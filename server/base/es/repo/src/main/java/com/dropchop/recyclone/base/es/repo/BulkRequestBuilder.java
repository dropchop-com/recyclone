package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.repo.config.ElasticIndexConfig;
import com.dropchop.recyclone.base.api.repo.config.HasIngestPipeline;
import com.dropchop.recyclone.base.api.repo.config.HasRootAlias;
import com.dropchop.recyclone.base.es.model.base.EsEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.Request;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import static com.dropchop.recyclone.base.api.model.base.Model.identifier;

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

  public <S extends EsEntity> StringBuilder buildBody(Collection<S> entities, StringBuilder bulkRequestBody) {
    try {
      //TODO: get index name from index config
      String indexName = "";
      for (S entity : entities) {
        bulkRequestBody
            .append("{ \"")
            .append(methodType.name().toLowerCase())
            .append("\" : { ")
            .append("\"_index\" : \"")
            .append(indexName)
            .append("\",")
            .append("\"_id\" : \"")
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

  public <S extends EsEntity> Request bulkRequest(Collection<S> entities) {
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
}
