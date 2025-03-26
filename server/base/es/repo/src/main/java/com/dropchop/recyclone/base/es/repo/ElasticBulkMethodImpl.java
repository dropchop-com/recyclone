package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.invoke.StatusMessage;
import com.dropchop.recyclone.base.es.model.base.EsEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.dropchop.recyclone.base.api.model.base.Model.identifier;

public abstract class ElasticBulkMethodImpl {
  protected abstract <S extends EsEntity> String getIndexOuterName(S entity);

  public enum MethodType {
    INDEX, DELETE, UPDATE
  }

  private final MethodType methodType;

  public ElasticBulkMethodImpl(MethodType methodType) {
    this.methodType = methodType;
  }

  public <S extends EsEntity> StringBuilder buildBulkRequest(
    Collection<S> entities,
    StringBuilder bulkRequestBody,
    ObjectMapper objectMapper) {

    try {
      for (S entity : entities) {
        bulkRequestBody
          .append("{ \"")
          .append(methodType.name().toLowerCase())
          .append("\" : { \"_index\" : \"")
          .append(getIndexOuterName(entity))
          .append("\", \"_id\" : \"")
          .append(identifier(entity))
          .append("\" } }\n");

        if (methodType.equals(MethodType.INDEX) || methodType.equals(MethodType.UPDATE)) {
          bulkRequestBody
            .append(objectMapper.writeValueAsString(entity))
            .append("\n");
        }
      }

      return bulkRequestBody;
    } catch (IOException e) {
      throw new ServiceException(
        ErrorCode.data_error, "Failed to serialize entity to JSON",
        Set.of(new AttributeString("error", e.getMessage()))
      );
    }
  }

  public <S extends Model> List<S> checkSuccessfulResponse(Collection<S> entities,
                                                           Response response,
                                                           ObjectMapper objectMapper) throws IOException {
    List<S> successfullyProcessedEntities = new ArrayList<>();
    List<StatusMessage> errorMessages = new ArrayList<>();
    List<S> entitiesToProcess = new ArrayList<>(entities);
    if (response.getStatusLine().getStatusCode() != 200) {
      throw new ServiceException(
        ErrorCode.internal_error,
        "Bulk request failed with status code: " + response.getStatusLine().getStatusCode()
      );
    }
    JsonNode responseBody = objectMapper.readTree(response.getEntity().getContent());
    if (responseBody.has("items")) {
      int i = 0;

      if(responseBody.has("errors")) {
        if(responseBody.get("errors").asBoolean()) {
          throw new ServiceException(
            ErrorCode.internal_error,
            "Bulk request failed with response: " + responseBody.get("items").toString()
          );
        }
      }

      for (JsonNode item : responseBody.get("items")) {
        // Check the type of operation (index, delete, etc.)
        JsonNode opResult = item.get("index") != null ? item.get("index") :
          item.get("delete") != null ? item.get("delete") : item.get("update");
        if (opResult != null) {
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
