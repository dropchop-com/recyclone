package com.dropchop.recyclone.quarkus.runtime.elasticsearch;

import co.elastic.clients.transport.rest5_client.low_level.Request;
import co.elastic.clients.transport.rest5_client.low_level.Response;
import co.elastic.clients.transport.rest5_client.low_level.ResponseException;
import co.elastic.clients.transport.rest5_client.low_level.ResponseListener;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import io.quarkus.runtime.configuration.MemorySize;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9/2/26.
 */
@ApplicationScoped
public class ElasticsearchDataApplier {

  private static final Logger log = LoggerFactory.getLogger(ElasticsearchDataApplier.class);
  private static final int MAX_CONCURRENT_REQUESTS = 6;

  @Inject
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  Rest5Client restClient;

  @Inject
  @ConfigProperty(name = "quarkus.recyclone.elasticsearch.init.bulk-request-size", defaultValue = "16M")
  MemorySize bulkRequestSize;

  private boolean checkTemplateExists(String templateUrl) throws IOException {
    Request request = new Request("GET", templateUrl);
    try {
      Response response = restClient.performRequest(request);
      return response.getStatusCode() != 404;
    } catch (IOException e) {
      if (e instanceof ResponseException responseException) {
        return responseException.getResponse().getStatusCode() != 404;
      }
      throw e;
    }
  }

  private CompletableFuture<Boolean> checkTemplateExistsAsync(String templateUrl) {
    Request request = new Request("GET", templateUrl);
    CompletableFuture<Boolean> future = new CompletableFuture<>();
    restClient.performRequestAsync(request, new ResponseListener() {
      @Override
      public void onSuccess(Response response) {
        future.complete(response.getStatusCode() != 404);
      }

      @Override
      public void onFailure(Exception exception) {
        if (exception instanceof ResponseException responseException) {
          future.complete(responseException.getResponse().getStatusCode() != 404);
        } else {
          future.completeExceptionally(exception);
        }
      }
    });
    return future;
  }

  private void apply(String url, String source, String method) throws IOException {
    Request request = new Request(method, url);
    if (source != null && !source.isBlank()) {
      request.setJsonEntity(source);
    }
    restClient.performRequest(request);
  }

  private CompletableFuture<Void> applyAsync(String url, String source, String method) {
    Request request = new Request(method, url);
    if (source != null && !source.isBlank()) {
      request.setJsonEntity(source);
    }

    CompletableFuture<Void> future = new CompletableFuture<>();
    restClient.performRequestAsync(request, new ResponseListener() {
      @Override
      public void onSuccess(Response response) {
        future.complete(null);
      }

      @Override
      public void onFailure(Exception exception) {
        future.completeExceptionally(exception);
      }
    });
    return future;
  }

  private void await(CompletableFuture<Void> future) throws IOException {
    try {
      future.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IOException("Interrupted while applying Elasticsearch bulk data", e);
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      if (cause instanceof IOException ioException) {
        throw ioException;
      }
      throw new IOException("Failed to apply Elasticsearch bulk data", cause);
    }
  }

  private void awaitAll(Deque<CompletableFuture<Void>> futures) throws IOException {
    IOException failure = null;
    while (!futures.isEmpty()) {
      try {
        await(futures.removeFirst());
      } catch (IOException e) {
        if (failure == null) {
          failure = e;
        } else {
          failure.addSuppressed(e);
        }
      }
    }
    if (failure != null) {
      throw failure;
    }
  }

  private void enqueue(Deque<CompletableFuture<Void>> inFlight,
                       Supplier<CompletableFuture<Void>> requestSupplier) throws IOException {
    if (inFlight.size() == MAX_CONCURRENT_REQUESTS) {
      try {
        await(inFlight.removeFirst());
      } catch (IOException e) {
        try {
          awaitAll(inFlight);
        } catch (IOException pendingFailure) {
          e.addSuppressed(pendingFailure);
        }
        throw e;
      }
    }
    inFlight.addLast(requestSupplier.get());
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

  private void requireDumpValue(String value, String fieldName, String templatePath, int lineNumber)
      throws IOException {
    if (value == null || value.isBlank()) {
      throw new IOException(
          "Invalid Elasticsearch dump JSON in [" + templatePath + "] at line [" + lineNumber
              + "]: missing field [" + fieldName + "]"
      );
    }
  }

  private Boolean bulkActionHasSource(String line) {
    String compactLine = line.replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    if (compactLine.startsWith("{\"delete\":")) {
      return false;
    }
    if (compactLine.startsWith("{\"index\":")
        || compactLine.startsWith("{\"create\":")
        || compactLine.startsWith("{\"update\":")) {
      return true;
    }
    return null;
  }

  private int queueBulkData(String templateName, String baseUrl, String bulkSource, long bulkSourceBytes,
                            int chunkIdx, Deque<CompletableFuture<Void>> inFlight) throws IOException {
    enqueue(inFlight, () -> applyAsync(baseUrl, bulkSource, "POST"));
    log.debug("Queued data [{}][{}] with [{}] bytes", templateName, chunkIdx, bulkSourceBytes);
    return chunkIdx + 1;
  }

  public boolean templateExists(String templateUrl) throws IOException {
    return checkTemplateExists(templateUrl);
  }

  public void applyTemplate(String templateUrl, String templateSource) throws IOException {
    apply(templateUrl, templateSource, "PUT");
  }

  public void refresh() throws IOException {
    apply("_refresh", null, "POST");
  }

  public void applyEmptyData(String templatePath, String templateSource) throws IOException {
    String json = templateSource.replace("{", "").replace("}", "").trim();
    String[] entries = json.split(",");

    int count = 0;
    String name = "";
    String unit = "";
    for (String entry : entries) {
      String[] parts = entry.split(":");
      String key = parts[0].replace("\"", "").trim();
      String value = parts[1].replace("\"", "").trim();
      switch (key) {
        case "count" -> count = Integer.parseInt(value);
        case "name" -> name = value;
        case "unit" -> unit = value;
      }
    }

    if (name.isBlank() || unit.isBlank()) {
      log.warn("Invalid template format [{}] missing name and unit", templatePath);
      return;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(name);
    Deque<CompletableFuture<Void>> inFlight = new ArrayDeque<>(MAX_CONCURRENT_REQUESTS);
    LocalDate now = LocalDate.now();
    for (int i = 0; i < count; i++) {
      LocalDate timeAgo;
      if (unit.equals("days")) {
        timeAgo = now.minusDays(i);
      } else if (unit.equals("months")) {
        timeAgo = now.minusMonths(i);
      } else {
        log.warn("Invalid template format [{}] unknown unit [{}]", templatePath, unit);
        continue;
      }
      String indexUrl = "/" + timeAgo.format(formatter);
      enqueue(inFlight, () -> checkTemplateExistsAsync(indexUrl)
          .thenCompose(exists -> exists
              ? CompletableFuture.completedFuture(null)
              : applyAsync(indexUrl, null, "PUT")));
    }
    awaitAll(inFlight);
  }

  public void applyData(String templateName, String templatePath, String templateSource, String baseUrl)
      throws IOException {
    List<String> lines = templateSource.lines().toList();
    String firstLine = lines.stream().filter(line -> !line.isBlank()).findFirst().orElse(null);
    if (firstLine == null) {
      log.warn("Invalid template format [{}]", templatePath);
      return;
    }
    boolean bulkFormat = bulkActionHasSource(firstLine) != null;

    long targetBytes = bulkRequestSize.asLongValue();
    if (targetBytes <= 0) {
      throw new IOException("Elasticsearch bulk request size must be greater than zero");
    }

    int chunkIdx = 0;
    long bulkBodyBytes = 0;
    StringBuilder bulkBody = new StringBuilder();
    Deque<CompletableFuture<Void>> inFlight = new ArrayDeque<>(MAX_CONCURRENT_REQUESTS);
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      if (line.isBlank()) {
        continue;
      }

      int lineNumber = i + 1;
      String operation;
      if (bulkFormat) {
        Boolean hasSource = bulkActionHasSource(line);
        if (hasSource == null) {
          throw new IOException(
              "Invalid Elasticsearch bulk JSON in [" + templatePath + "] at line ["
                  + lineNumber + "]: expected an action line"
          );
        }

        StringBuilder operationBuilder = new StringBuilder(line).append("\n");
        if (hasSource) {
          if (++i >= lines.size() || lines.get(i).isBlank()) {
            throw new IOException(
                "Invalid Elasticsearch bulk JSON in [" + templatePath + "] at line ["
                    + lineNumber + "]: missing source line"
            );
          }
          operationBuilder.append(lines.get(i)).append("\n");
        }
        operation = operationBuilder.toString();
      } else {
        String indexName = extractFieldValue("_index", line);
        String id = extractFieldValue("_id", line);
        String sourceString = extractSource(line);
        requireDumpValue(indexName, "_index", templatePath, lineNumber);
        requireDumpValue(id, "_id", templatePath, lineNumber);
        requireDumpValue(sourceString, "_source", templatePath, lineNumber);

        operation = "{ \"index\" : { \"_index\" : \"" + indexName + "\", \"_id\" : \"" + id
            + "\" } }\n" + sourceString + "\n";
      }

      int operationBytes = operation.getBytes(UTF_8).length;
      if (bulkBodyBytes > 0 && bulkBodyBytes + operationBytes > targetBytes) {
        chunkIdx = queueBulkData(
            templateName, baseUrl, bulkBody.toString(), bulkBodyBytes, chunkIdx, inFlight
        );
        bulkBody.setLength(0);
        bulkBodyBytes = 0;
      }
      bulkBody.append(operation);
      bulkBodyBytes += operationBytes;
    }
    if (!bulkBody.isEmpty()) {
      chunkIdx = queueBulkData(
          templateName, baseUrl, bulkBody.toString(), bulkBodyBytes, chunkIdx, inFlight
      );
    }
    awaitAll(inFlight);
    log.debug("Applied data [{}] in [{}] chunks", templateName, chunkIdx);
  }
}
