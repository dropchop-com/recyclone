package com.dropchop.recyclone.quarkus.runtime.elasticsearch;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/27/25.
 */
@ApplicationScoped
public class ElasticSearchTestHelper {

  private static final Logger log = LoggerFactory.getLogger(ElasticSearchTestHelper.class);

  @Inject
  @RecycloneType(RECYCLONE_DEFAULT)
  ObjectMapper objectMapper;

  @Inject
  RestClient restClient;

  public void waitForObjects(String url, Collection<String> ids, int expectedSize, long timeout)
      throws IOException {
    int inc = 100;
    int waited = 0;
    while (true) {
      try {
        StringBuilder stringBuilder = new StringBuilder();
        for(String id : ids) {
          if (!stringBuilder.isEmpty()) {
            stringBuilder.append(",");
          }
          stringBuilder.append("\"").append(id).append("\"");
        }
        Request request = new Request("POST", url);
        String json = "{\"query\": {\"terms\": {\"_id\": [" + stringBuilder + "]}}}";
        request.setJsonEntity(json);
        Response response = restClient.performRequest(request);
        if (response.getStatusLine().getStatusCode() != 200) {
          //noinspection BusyWait
          Thread.sleep(inc);
          continue;
        }
        try {
          String responseBody = EntityUtils.toString(response.getEntity());
          JsonNode jsonResponse = objectMapper.readTree(responseBody);
          JsonNode node = jsonResponse.get("hits");
          if (node == null) {
            //noinspection BusyWait
            Thread.sleep(inc);
            continue;
          }
          node = node.get("total");
          if (node == null) {
            //noinspection BusyWait
            Thread.sleep(inc);
            continue;
          }
          int totalHits = node.get("value").asInt();
          if (totalHits == expectedSize) {
            break;
          }
        } catch (Exception e) {
          log.warn("Exception while waiting for [{}] at [{}]", json, url, e);
        }
      } catch (InterruptedException ignored) {
        waited += inc;
        if (waited >= timeout) {
          break;
        }
      }
    }
  }

  public void waitForObjects(String url, Collection<String> ids, int expectedSize) throws IOException {
    waitForObjects(url, ids, expectedSize,10000);
  }

  public void waitForObjects(String url, String id, int expectedSize) throws IOException {
    waitForObjects(url, Set.of(id), expectedSize,10000);
  }

  public void waitForObject(String url, String id) throws IOException {
    waitForObjects(url, Set.of(id), 1,10000);
  }
}
