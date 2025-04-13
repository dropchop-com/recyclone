package com.dropchop.recyclone.quarkus.runtime.elasticsearch;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/21/25.
 */
@ApplicationScoped
public class ElasticsearchInitializer {

  private final Logger log = LoggerFactory.getLogger(ElasticsearchInitializer.class);
  private static final String baseDir = "elasticsearch-it";

  @Inject
  RestClient restClient;

  private static class Template {
    public final String name;
    public final String templatePath;
    public final String template;

    public Template(String nameOrFileName, String classPath, String template) {
      int pos = nameOrFileName.lastIndexOf(".");
      if (pos > 0) {
        this.name = nameOrFileName.substring(0, pos);
      } else {
        this.name = nameOrFileName;
      }
      this.templatePath = classPath;
      this.template = template;
    }
  }

  private static class Templates {
    public final String resourcePath;
    public final String baseUrl;
    public final List<Template> templates = new ArrayList<>();

    public Templates(String resourcePath, String baseUrl) {
      this.resourcePath = resourcePath;
      this.baseUrl = baseUrl;
    }
  }

  private static class IngestPipeline extends Templates {
    public IngestPipeline() {
      super(baseDir + "/ingest-pipeline", "_ingest/pipeline");
    }
  }

  private static class ComponentTemplate extends Templates {
    public ComponentTemplate() {
      super(baseDir + "/component-template", "_component_template");
    }
  }

  private static class IlmPolicy extends Templates {
    public IlmPolicy() {
      super(baseDir + "/ilm-policy", "_ilm/policy");
    }
  }

  private static class IndexTemplate extends Templates {
    public IndexTemplate() {
      super(baseDir + "/index-template", "_index_template");
    }
  }

  private static class Data extends Templates {
    public Data() {
      super(baseDir + "/data", "_bulk");
    }
  }

  private final Collection<Templates> templatesList = List.of(
      new IngestPipeline(),
      new ComponentTemplate(),
      new IlmPolicy(),
      new IndexTemplate(),
      new Data()
  );

  private void loadTemplateResources(String profileKey) throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    for (Templates templates : templatesList) {
      Enumeration<URL> resources = classLoader.getResources(templates.resourcePath);
      while (resources.hasMoreElements()) {
        URL resourceUrl = resources.nextElement();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), UTF_8))) {
          String resource;
          log.debug("Reading template folder [{}]", resourceUrl);
          while ((resource = br.readLine()) != null) {
            if (resource.startsWith("%") && !resource.startsWith("%" + profileKey + ".")) {
              log.debug("Skipping profile [{}] template [{}]", profileKey, resource);
              continue;
            }
            String templatePath = templates.resourcePath + "/" + resource;
            log.debug("Reading template [{}]", resource);
            try (InputStream is = classLoader.getResourceAsStream(templatePath)) {
              if (is == null) {
                log.error("Unable to read null template contents [{}]", templatePath);
                continue;
              }
              String text = new String(is.readAllBytes(), UTF_8);
              Template template = new Template(resource, templatePath, text);
              templates.templates.add(template);
            } catch (IOException e) {
              log.error("Unable to read template [{}]", templatePath, e);
            }
            log.info("Read template path [{}]", resource);
          }
        } catch (IOException e) {
          log.error("Unable to read template path [{}]", resourceUrl, e);
        }
      }
    }
  }

  private boolean checkTemplateExists(String templateUrl) throws IOException {
    Request request = new Request("GET", templateUrl);
    try {
      Response response = restClient.performRequest(request);
      int statusCode = response.getStatusLine().getStatusCode();
      return statusCode != 404;
    } catch (IOException e) {
      if (e instanceof ResponseException re) {
        int statusCode = re.getResponse().getStatusLine().getStatusCode();
        return statusCode != 404;
      }
      throw e;
    }
  }

  private void apply(String templateUrl, String templateSource, String method) throws IOException {
    Request request = new Request(method, templateUrl);
    request.setJsonEntity(templateSource);
    restClient.performRequest(request);
  }

  private void applyTemplate(String templateUrl, String templateSource) throws IOException {
    apply(templateUrl, templateSource, "PUT");
  }

  private String extractSource(String doc) {
    String sourceField = "\"_source\":";
    int idx = doc.indexOf(sourceField);
    if (idx == -1) {
      return null;
    }
    idx = doc.indexOf('{', idx);
    if (idx == -1) {
      return null;
    }
    return doc.substring(idx, doc.length() - 1);
  }

  private String extractFieldValue(String fieldName, String doc) {
    int idx = doc.indexOf(fieldName);
    if (idx == -1) {
      return null;
    }
    idx = doc.indexOf('"', idx + fieldName.length() + 1); // third " in "_field_name": "value"
    if (idx == -1) {
      return null;
    }
    int endIdx = doc.indexOf('"', idx + 1); // fourth " in "_field_name": "value"
    return doc.substring(idx + 1, endIdx);
  }

  private void applyData(Template template, String baseUrl) throws IOException {
    List<String> lines = Arrays.asList(template.template.split("\n"));
    int chunkSize = 2500;
    int chunkIdx = 0;
    int maxChunkIdx = lines.size() / chunkSize;
    for (int i = 0; i < lines.size(); i += chunkSize) {
      List<String> chunk = lines.subList(i, Math.min(i + chunkSize, lines.size()));
      String bulkRequestBody;
      if (template.templatePath.endsWith(".json")) {
        // why would it be simple? this is an elastic dump file with index doc per line
        StringBuilder bulkBody = new StringBuilder();
        for (String line : chunk) {
          String indexName = extractFieldValue("_index", line);
          String id = extractFieldValue("_id", line);
          String sourceString = extractSource(line);

          bulkBody.append("{ \"index\" : { \"_index\" : \"");
          bulkBody.append(indexName);
          bulkBody.append("\", \"_id\" : \"");
          bulkBody.append(id);
          bulkBody.append("\" } }\n");
          bulkBody.append(sourceString);
          bulkBody.append("\n");
        }
        bulkRequestBody = bulkBody.toString();
      } else {
        // assuming the proper bulk format
        bulkRequestBody = String.join("\n", chunk);
      }
      if (chunkIdx == maxChunkIdx) { // last chunk
        apply(baseUrl + "?refresh=wait_for", bulkRequestBody, "POST");
      } else {
        apply(baseUrl, bulkRequestBody, "POST");
      }
      log.debug("Applied data [{}][{}]", template.name, chunkIdx);
      chunkIdx++;
    }
  }

  private void applyMissingTemplates() {
    for (Templates templates : templatesList) {
      if (templates instanceof Data dataTemplate) {
        for (Template template : templates.templates) {
          try {
            applyData(template, dataTemplate.baseUrl);
          } catch (IOException e) {
            log.error("Failed to apply data template [{}]!", template.name, e);
          }
        }
      } else {
        for (Template template : templates.templates) {
          String templateUrl = templates.baseUrl + "/" + template.name;
          try {
            log.debug("Applying template [{}]", templateUrl);
            if (!checkTemplateExists(templateUrl)) {
              applyTemplate(templateUrl, template.template);
            } else {
              log.info("Template [{}] already exists", templateUrl);
            }
          } catch (IOException e) {
            log.error("Failed to apply template [{}]!", templateUrl, e);
          }
        }
      }
    }
  }

  public void onStart(@Observes StartupEvent event) throws IOException {
    if (!LaunchMode.current().isDevOrTest()) {
      return;
    }
    String profileKey = LaunchMode.current().getDefaultProfile();
    loadTemplateResources(profileKey);
    applyMissingTemplates();
  }
}
