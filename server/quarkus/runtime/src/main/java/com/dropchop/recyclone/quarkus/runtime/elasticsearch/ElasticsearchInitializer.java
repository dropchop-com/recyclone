package com.dropchop.recyclone.quarkus.runtime.elasticsearch;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.eventbus.EventBus;
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
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dropchop.recyclone.base.api.model.invoke.Constants.Messages.CACHE_STORAGE_INIT;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/21/25.
 */
@ApplicationScoped
public class ElasticsearchInitializer {

  private static final Logger log = LoggerFactory.getLogger(ElasticsearchInitializer.class);

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
      super("ingest-pipeline", "_ingest/pipeline");
    }
  }

  private static class ComponentTemplate extends Templates {
    public ComponentTemplate() {
      super("component-template", "_component_template");
    }
  }

  private static class IlmPolicy extends Templates {
    public IlmPolicy() {
      super("ilm-policy", "_ilm/policy");
    }
  }

  private static class IndexTemplate extends Templates {
    public static final String BASE_URL = "_index_template";

    public IndexTemplate() {
      super("index-template", BASE_URL);
    }
  }

  private static class Data extends Templates {
    public Data() {
      super("data", "_bulk");
    }
  }

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  RestClient restClient;

  private final Template markerTemplate = new Template(
      ".initialized_marker_template", null,
      "{ \"index_patterns\": [\".initialized_marker_template\"], \"template\": {} }"
  );

  private final Collection<Templates> templatesList = List.of(
      new IngestPipeline(),
      new ComponentTemplate(),
      new IlmPolicy(),
      new IndexTemplate(),
      new Data()
  );

  private void loadTemplateResources(String profileKey, Path initPath) throws IOException {
    for (Templates templates : templatesList) {
      Path resourcePath = initPath.resolve(templates.resourcePath);
      if (!Files.exists(resourcePath)) {
        continue;
      }
      if (!Files.isReadable(resourcePath)) {
        log.warn("Unable to read templates folder [{}]", resourcePath);
        continue;
      }
      try (Stream<Path> paths = Files.list(resourcePath)) {
        paths.filter(Files::isRegularFile)
            .filter(Files::isReadable)
            .forEach(filePath -> {
              String resource = filePath.getFileName().toString();
              if (resource.startsWith("%") && !resource.startsWith("%" + profileKey + ".")) {
                log.debug("Skipping non [{}] profile template [{}]", profileKey, resource);
                return;
              }
              try (BufferedReader br = Files.newBufferedReader(filePath, UTF_8)) {
                int nameCount = filePath.getNameCount();
                Path lastTwo = nameCount >= 2
                    ? filePath.subpath(nameCount - 2, nameCount)
                    : filePath;
                //log.debug("Reading template [{}]", lastTwo);
                String text = br.lines().collect(Collectors.joining("\n"));
                Template template = new Template(resource, filePath.toAbsolutePath().toString(), text);
                templates.templates.add(template);
                log.info("Read template path [{}]", lastTwo);
              } catch (IOException e) {
                throw new UncheckedIOException(e);
              }
            });
      } catch (UncheckedIOException e) {
        throw e.getCause();
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

  private boolean checkInitMarkerTemplateExists() throws IOException {
    return checkTemplateExists(IndexTemplate.BASE_URL + "/" + markerTemplate.name);
  }

  private void apply(String templateUrl, String templateSource, String method) throws IOException {
    Request request = new Request(method, templateUrl);
    request.setJsonEntity(templateSource);
    restClient.performRequest(request);
  }

  private void applyTemplate(String templateUrl, String templateSource) throws IOException {
    apply(templateUrl, templateSource, "PUT");
  }

  private void applyInitMarkerTemplate() throws IOException {
    applyTemplate(IndexTemplate.BASE_URL + "/" + markerTemplate.name, markerTemplate.template);
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
    if (lines.size() <= 1) {
      log.warn("Invalid template format [{}]", template.templatePath);
      return;
    }
    boolean bulkFormat = lines
        .getFirst()
        .replace("\\s+", "")
        .toLowerCase()
        .startsWith("{\"index\":");

    //lazy heuristics i.e. 2500 chunkSize can fail if docs are big
    //(default is 100 mb fro http.max_content_length in your elasticsearch.yml)
    //bulk format has command + \n + content sequence so we need to stop at an odd index
    int chunkSize = 2499;
    int chunkIdx = 0;
    int maxChunkIdx = lines.size() / chunkSize;
    for (int i = 0; i < lines.size(); i += chunkSize) {
      List<String> chunk = lines.subList(i, Math.min(i + chunkSize, lines.size()));
      StringBuilder bulkBody = new StringBuilder();
      if (!bulkFormat) {
        // why would it be simple? this is an elastic dump file with index doc per line
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
      } else {
        for (String line : chunk) {
          if (line.isBlank()) {
            continue;
          }
          bulkBody.append(line);
          bulkBody.append("\n");
        }
      }
      if (chunkIdx == maxChunkIdx) { // last chunk
        apply(baseUrl + "?refresh=wait_for", bulkBody.toString(), "POST");
      } else {
        apply(baseUrl, bulkBody.toString(), "POST");
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

  private Path isDockerFolder(Path currentDir) {
    log.info("Searching for container init folder in [{}]", currentDir);
    Path dockerFolder = currentDir.resolve(Path.of("docker", "elasticsearch", "init.d"));
    if (Files.exists(dockerFolder) && Files.isReadable(dockerFolder)) {
      log.info("Found container init folder [{}]", dockerFolder);
      return dockerFolder;
    }
    dockerFolder = currentDir.resolve(Path.of("src", "main", "docker", "elasticsearch", "init.d"));
    if (Files.exists(dockerFolder) && Files.isReadable(dockerFolder)) {
      log.info("Found container init folder [{}]", dockerFolder);
      return dockerFolder;
    }
    return null;
  }

  private Path searchForDockerFolder(Path runDir) {
    Path dockerFolder = isDockerFolder(runDir);
    while (dockerFolder == null) {
      runDir = runDir.getParent();
      if (runDir == null) {
        return null;
      }
      Path pom = runDir.resolve(Path.of("pom.xml"));
      if (Files.exists(pom)) {
        dockerFolder = isDockerFolder(runDir);
      } else {
        break;
      }
    }
    return dockerFolder;
  }

  @Inject
  EventBus eventBus;

  public void onStart(@Observes StartupEvent event) throws IOException {
    if (!LaunchMode.current().isDevOrTest()) {
      eventBus.send(CACHE_STORAGE_INIT, "skipped");
      return;
    }

    Path path = Paths.get(System.getProperty("user.dir"));
    Path configPath = searchForDockerFolder(path);
    if (configPath == null) {
      log.info("Unable to find Elasticsearch configuration folder in skipping initialization.");
      return;
    }

    String profileKey = LaunchMode.current().getDefaultProfile();
    if (checkInitMarkerTemplateExists()) {
      log.info("Elasticsearch already initialized, the marker template [{}] exists!", markerTemplate.name);
      eventBus.send(CACHE_STORAGE_INIT, "initialized");
      return;
    }
    loadTemplateResources(profileKey, configPath);
    applyMissingTemplates();
    applyInitMarkerTemplate();
    eventBus.send(CACHE_STORAGE_INIT, "initialized");
  }
}
