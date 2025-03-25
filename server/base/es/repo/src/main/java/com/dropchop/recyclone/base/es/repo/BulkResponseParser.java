package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.invoke.StatusMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/25/25.
 */
@RequiredArgsConstructor
public class BulkResponseParser {

  private final ObjectMapper objectMapper;

  public <S extends Model> List<S> parseResponse(Collection<S> entities, Response response) {
    List<S> successfullyProcessedEntities = new ArrayList<>();
    List<StatusMessage> errorMessages = new ArrayList<>();
    List<S> entitiesToProcess = new ArrayList<>(entities);
    if (response.getStatusLine().getStatusCode() != 200) {
      throw new ServiceException(
          ErrorCode.internal_error,
          "Bulk request failed with status code: " + response.getStatusLine().getStatusCode()
      );
    }
    JsonNode responseBody;
    try {
      responseBody = objectMapper.readTree(response.getEntity().getContent());
    } catch (Exception e) {
      throw new ServiceException(
          ErrorCode.internal_error, "Bulk response parse to tree failed", e
      );
    }
    if (responseBody.has("items")) {
      int i = 0;
      for (JsonNode item : responseBody.get("items")) {
        // Check the type of operation (index, delete, etc.)
        JsonNode opResult = item.get("index") != null ? item.get("index") :
            item.get("delete") != null ? item.get("delete") : item.get("update");
        if (opResult == null) {
          continue;
        }
        String result = opResult.get("result").asText();
        boolean isSuccess = "created".equals(result) || "updated".equals(result) || "deleted".equals(result);
        if (isSuccess) {
          successfullyProcessedEntities.add(entitiesToProcess.get(i));
        } else {
          String error = opResult.has("error") ? opResult.get("error").toString() : "Unknown error";
          int statusCode = opResult.has("status") ? opResult.get("status").asInt() : -1;
          errorMessages.add(new StatusMessage(
              ErrorCode.process_error, "Failed to process entity " + i,
              Set.of(
                  new AttributeString("status", String.valueOf(statusCode)),
                  new AttributeString("error", error)
              )
          ));
        }
        i++;
      }
    }

    if (entities.equals(successfullyProcessedEntities)) {
      return successfullyProcessedEntities;
    } else {
      throw new ServiceException(errorMessages);
    }
  }
}
