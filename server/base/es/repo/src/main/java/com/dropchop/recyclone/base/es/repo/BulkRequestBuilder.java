package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.repo.config.*;
import com.dropchop.recyclone.base.es.model.base.EsEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.Request;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import static com.dropchop.recyclone.base.api.model.base.Model.identifier;
import static com.dropchop.recyclone.base.es.repo.BulkRequestBuilder.MethodType.INDEX;
import static com.dropchop.recyclone.base.es.repo.BulkRequestBuilder.MethodType.UPDATE;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/25/25.
 */
@Getter
@RequiredArgsConstructor
public class BulkRequestBuilder {

  public static final String BULK_ENDPOINT = "/_bulk";
  public static final String HTTP_POST = "POST";

  public enum MethodType {
    INDEX, UPDATE, DELETE, DELETE_BY_ID
  }

  private final MethodType methodType;
  private final ObjectMapper objectMapper;
  private final ElasticIndexConfig indexConfig;

  public StringBuilder buildBody(Collection<?> entities, StringBuilder bulkRequestBody) {

      String indexName = null;
      ElasticIndexConfig indexConfig = getIndexConfig();
      HasEntityBasedWriteIndex computeName = null;
      if (!(indexConfig instanceof HasIngestPipeline)) {
        if (indexConfig instanceof HasStaticWriteIndex hasStaticWriteIndex) {
          indexName = hasStaticWriteIndex.getWriteIndex();
        } else if (indexConfig instanceof HasEntityBasedWriteIndex hasEntityBasedWriteIndex) {
          computeName = hasEntityBasedWriteIndex;
        }
      }

      for (Object obj : entities) {
        String id;
        String value = null;
        if (obj instanceof EsEntity entity) {
          id = identifier(entity);
          if (computeName != null) {
            indexName = computeName.getWriteIndex(entity);
          }
          try {
            value = objectMapper.writeValueAsString(entity);
          } catch (IOException e) {
            throw new ServiceException(
                ErrorCode.data_error, "Failed to serialize entity to JSON",
                Set.of(
                    new AttributeString("id", String.valueOf(id)),
                    new AttributeString("error", e.getMessage())
                )
            );
          }
        } else {
          if (INDEX.equals(methodType) || UPDATE.equals(methodType)) {
            throw new ServiceException(
                ErrorCode.internal_error,
                "Bulk index or update makes no sense if you passed IDs as parameters!"
            );
          }
          id = String.valueOf(obj);
        }

        bulkRequestBody
            .append("{ \"")
            .append(methodType.name().toLowerCase())
            .append("\" : { ");
        if (indexName != null) {
          bulkRequestBody
              .append("\"_index\" : \"")
              .append(indexName)
              .append("\",");
        }
        bulkRequestBody
            .append("\"_id\" : \"")
            .append(id)
            .append("\" } }\n");

        if (value != null && (INDEX.equals(methodType) || UPDATE.equals(methodType))) {
          bulkRequestBody
              .append(value)
              .append("\n");
        }
      }

      return bulkRequestBody;
  }

  /**
   * Build bulk request based on collection of IDs or collection of entities.
   */
  public Request bulkRequest(Collection<?> entities) {
    StringBuilder bulkRequestBody = new StringBuilder();
    StringBuilder endpoint = new StringBuilder();
    if (indexConfig instanceof HasRootAlias hasRootAlias) {
      endpoint.append("/");
      endpoint.append(hasRootAlias.getRootAlias());
    }
    endpoint.append(BULK_ENDPOINT);

    if(indexConfig instanceof HasIngestPipeline hasIngestPipeline) {
      endpoint.append("?pipeline=").append(hasIngestPipeline.getIngestPipeline());
    }

    this.buildBody(entities, bulkRequestBody);

    Request request = new Request(HTTP_POST, endpoint.toString());
    request.setJsonEntity(bulkRequestBody.toString());
    return request;
  }

  /*public <X extends ID> int deleteById(Collection<X> ids) {
    if (ids == null || ids.isEmpty()) {
      return 0;
    }

    Class<E> rootClass = getRootClass();
    StringBuilder bulkRequestBody = new StringBuilder();
    ObjectMapper objectMapper = getObjectMapper();
    ElasticIndexConfig indexConfig = getElasticIndexConfig();
    String defaultAlias = indexConfig.getDefaultAlias();
    for (X id : ids) {
      bulkRequestBody
        .append("{ \"delete\" : { \"_index\" : \"")
        .append(indexConfig.getIndexName(rootClass))
        .append("\", \"_id\" : \"")
        .append(id.toString())
        .append("\" } }\n");
    }

    Request request = new Request("POST", "/_bulk");
    request.setJsonEntity(bulkRequestBody.toString());

    try {
      Response response = getElasticsearchClient().performRequest(request);
      JsonNode responseBody = objectMapper.readTree(response.getEntity().getContent());

      int deletedCount = 0;
      if (responseBody.has("items")) {
        for (JsonNode item : responseBody.get("items")) {
          JsonNode deleteResult = item.get("delete");
          if (deleteResult != null && "deleted".equals(deleteResult.get("result").asText())) {
            deletedCount++;
          }
        }
      }

      return deletedCount;
    } catch (IOException e) {
      throw new ServiceException(
        ErrorCode.data_error,
        "Failed to delete entities by ID",
        Set.of(new AttributeString("error", e.getMessage()))
      );
    }
    return 0;
  }*/
}
