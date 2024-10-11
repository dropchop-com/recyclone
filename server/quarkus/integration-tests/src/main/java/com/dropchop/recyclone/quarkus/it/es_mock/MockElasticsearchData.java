package com.dropchop.recyclone.quarkus.it.es_mock;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

@ApplicationScoped
public class MockElasticsearchData {

  @Inject
  RestClient lowLevelClient;

  public void loadData(@Observes StartupEvent event) throws IOException {
    // Mock data
    String jsonData = "{ \"name\": \"Test Document\", \"content\": \"This is a test\" }";
    Request request = new Request("PUT", "/dummy/_doc/1");
    request.setJsonEntity(jsonData);
    lowLevelClient.performRequest(request);
  }
}
