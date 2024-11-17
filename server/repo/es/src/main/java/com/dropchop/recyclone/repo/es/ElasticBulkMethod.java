package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.invoke.StatusMessage;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ElasticBulkMethod {


  public enum MethodType {
    INDEX, DELETE, UPDATE
  }

  private final MethodType methodType;
  private final String index;
  public ElasticBulkMethod(MethodType methodType, String index) {
    this.methodType = methodType;
    this.index = index;
  }

  public <S> StringBuilder buildBulkRequest(
    Collection<S> entities,
    StringBuilder bulkRequestBody,
    ObjectMapper objectMapper) {

    try {
      for (S entity : entities) {
        bulkRequestBody
          .append("{ \"")
          .append(methodType.name().toLowerCase())
          .append("\" : { \"_index\" : \"")
          .append(index)
          .append("\", \"_id\" : \"")
          .append(getEntityId(entity))
          .append("\" } }\n");

        if(methodType.equals(MethodType.INDEX) || methodType.equals(MethodType.UPDATE)) {
          bulkRequestBody
            .append(objectMapper.writeValueAsString(entity))
            .append("\n");
        }
      }

      return bulkRequestBody;
    } catch (IOException e) {
      throw new ServiceException(new StatusMessage(
        ErrorCode.data_error,
        "Failed to serialize entity to JSON",
        Set.of(new AttributeString("error", e.getMessage()))));
    }
  }

  public <S> List<S> checkSuccessfulResponse(
    Collection<S> entities,
    Response response,
    ObjectMapper objectMapper) throws IOException {

    List<S> successfullyProcessedEntities = new ArrayList<>();
    List<StatusMessage> errorMessages = new ArrayList<>();

    if (response.getStatusLine().getStatusCode() == 200) {
      JsonNode responseBody = objectMapper.readTree(response.getEntity().getContent());
      if (responseBody.has("items")) {
        int i = 0;
        for (JsonNode item : responseBody.get("items")) {
          // Check the type of operation (index, delete, etc.)
          JsonNode operationResult = item.get("index") != null ? item.get("index") :
            item.get("delete") != null ? item.get("delete") :
              item.get("update");

          if (operationResult != null) {
            String result = operationResult.get("result").asText();
            boolean isSuccess = "created".equals(result) || "updated".equals(result) || "deleted".equals(result);

            if (isSuccess) {
              successfullyProcessedEntities.add((S) entities.toArray()[i]);
            } else {
              String error = operationResult.has("error") ? operationResult.get("error").toString() : "Unknown error";
              int statusCode = operationResult.has("status") ? operationResult.get("status").asInt() : -1;
              errorMessages.add(new StatusMessage(
                ErrorCode.process_error,
                "Failed to process entity " + i,
                Set.of(
                  new AttributeString("status", String.valueOf(statusCode)),
                  new AttributeString("error", error)
                )
              ));
            }
          }
          i++;
        }
      }

      if (entities.equals(successfullyProcessedEntities)) {
        return successfullyProcessedEntities;
      } else {
        throw new ServiceException(errorMessages);
      }
    } else {
      throw new ServiceException(
        new StatusMessage(
          ErrorCode.internal_error,
          "Bulk request failed with status code: " + response.getStatusLine().getStatusCode(),
          null
        )
      );
    }
  }


  protected <S> String getEntityId(S entity) {
    if (entity instanceof HasUuid uEntity) {
      return uEntity.getUuid().toString();
    } if (entity instanceof HasCode cEntity) {
      return cEntity.getCode();
    } else {
      throw new UnsupportedOperationException(
        "Missing implementation for getEntityId for entity type [" + entity.getClass() + "]"
      );
    }
  }
}
