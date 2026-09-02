package com.dropchop.recyclone.quarkus.runtime.elasticsearch;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
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
    private final String name;
    private final String templatePath;
    private final String template;

    private Template(String nameOrFileName, String classPath, String template) {
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
    private final String resourcePath;
    private final String baseUrl;
    private final List<Template> templates = new ArrayList<>();

    private Templates(String resourcePath, String baseUrl) {
      this.resourcePath = resourcePath;
      this.baseUrl = baseUrl;
    }
  }

  private static class IngestPipeline extends Templates {
    private IngestPipeline() {
      super("ingest-pipeline", "_ingest/pipeline");
    }
  }

  private static class ComponentTemplate extends Templates {
    private ComponentTemplate() {
      super("component-template", "_component_template");
    }
  }

  private static class IlmPolicy extends Templates {
    private IlmPolicy() {
      super("ilm-policy", "_ilm/policy");
    }
  }

  private static class IndexTemplate extends Templates {
    private static final String BASE_URL = "_index_template";

    private IndexTemplate() {
      super("index-template", BASE_URL);
    }
  }

  private static class Data extends Templates {
    private Data() {
      super("data", "_bulk");
    }
  }

  private static class EmptyData extends Templates {
    private EmptyData() {
      super("empty", "");
    }
  }

  private final Template markerTemplate = new Template(
      ".initialized_marker_template", null,
      "{ \"index_patterns\": [\".initialized_marker_template\"], \"template\": {} }"
  );

  private final Collection<Templates> templatesList = List.of(
      new IngestPipeline(),
      new ComponentTemplate(),
      new IlmPolicy(),
      new IndexTemplate(),
      new EmptyData(),
      new Data()
  );

  @Inject
  ElasticsearchDataApplier dataApplier;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  EventBus eventBus;

  private Path isDockerFolder(Path currentDir) {
    log.info("Searching for container init folder in [{}]", currentDir);
    Path dockerFolder = currentDir.resolve(Path.of("docker", "elasticsearch", "init.d"));
    if (Files.exists(dockerFolder) && Files.isReadable(dockerFolder)) {
      log.info("Found container init folder [{}]", dockerFolder);
      return dockerFolder;
    }
    dockerFolder = currentDir.resolve(Path.of("config", "docker", "elasticsearch", "init.d"));
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
              if (templates instanceof Data && !resource.toLowerCase(Locale.ROOT).endsWith(".jsonl")) {
                log.debug("Skipping non JSONL data template [{}]", resource);
                return;
              }
              try (BufferedReader br = Files.newBufferedReader(filePath, UTF_8)) {
                int nameCount = filePath.getNameCount();
                Path lastTwo = nameCount >= 2
                    ? filePath.subpath(nameCount - 2, nameCount)
                    : filePath;
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

  private boolean initMarkerTemplateExists() throws IOException {
    return dataApplier.templateExists(IndexTemplate.BASE_URL + "/" + markerTemplate.name);
  }

  private void initializeMarkerTemplate() throws IOException {
    dataApplier.applyTemplate(
        IndexTemplate.BASE_URL + "/" + markerTemplate.name, markerTemplate.template
    );
  }

  private void initializeTemplates() {
    for (Templates templates : templatesList) {
      if (templates instanceof Data) {
        for (Template template : templates.templates) {
          try {
            dataApplier.applyData(
                template.name, template.templatePath, template.template, templates.baseUrl
            );
          } catch (IOException e) {
            log.error("Failed to apply data template [{}]!", template.name, e);
          }
        }
      } else if (templates instanceof EmptyData) {
        for (Template template : templates.templates) {
          try {
            dataApplier.applyEmptyData(template.templatePath, template.template);
          } catch (IOException e) {
            log.error("Failed to apply data template [{}]!", template.name, e);
          }
        }
      } else {
        for (Template template : templates.templates) {
          String templateUrl = templates.baseUrl + "/" + template.name;
          try {
            log.debug("Applying template [{}]", templateUrl);
            if (!dataApplier.templateExists(templateUrl)) {
              dataApplier.applyTemplate(templateUrl, template.template);
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
    if (initMarkerTemplateExists()) {
      log.info("Elasticsearch already initialized, the marker template [{}] exists!", markerTemplate.name);
      eventBus.send(CACHE_STORAGE_INIT, "initialized");
      return;
    }
    loadTemplateResources(profileKey, configPath);
    initializeTemplates();
    initializeMarkerTemplate();
    dataApplier.refresh();
    eventBus.send(CACHE_STORAGE_INIT, "initialized");
  }
}
