package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import com.dropchop.recyclone.model.api.utils.Strings;
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.es.mapper.ElasticSearchResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class ElasticRepository<E, ID> implements CrudRepository<E, ID> {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  ExecContextContainer ctxContainer;

  protected Collection<CriteriaDecorator> getCommonCriteriaDecorators() {
    return List.of(
      new PageCriteriaDecorator()
    );
  }

  @Override
  public RepositoryExecContext<E> getRepositoryExecContext() {
    ElasticExecContext<E> context = new ElasticExecContext<E>().of(ctxContainer.get());
    for (CriteriaDecorator decorator : getCommonCriteriaDecorators()) {
      context.decorateWith(decorator);
    }
    return context;
  }

  @Override
  public RepositoryExecContext<E> getRepositoryExecContext(MappingContext mappingContext) {
    return getRepositoryExecContext().totalCount(mappingContext);
  }

  protected String getIndexName() {
    return Strings.toSnakeCase(getRootClass().getSimpleName());
  }

  protected <S extends E> String getEntityId(S entity) {
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

  @Override
  public <S extends E> List<S> save(Collection<S> entities) {
    if (entities == null || entities.isEmpty()) {
      return Collections.emptyList();
    }

    StringBuilder bulkRequestBody = new StringBuilder();

    try {
      String indexName = getIndexName();
      for (S entity : entities) {
        bulkRequestBody.append("{ \"index\" : { \"_index\" : \"")
            .append(indexName)
            .append("\", \"_id\" : \"")
            .append(getEntityId(entity))
            .append("\" } }\n");
        bulkRequestBody.append(getObjectMapper().writeValueAsString(entity)).append("\n");
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to serialize entity to JSON", e);
    }

    Request request = new Request("POST", "/_bulk");
    request.setJsonEntity(bulkRequestBody.toString());
    //TODO: what elasticsearch bulk request returns? ***
    try {
      Response response = getElasticsearchClient().performRequest(request);

      if(response.getStatusLine().getStatusCode() == 200) {
        return List.copyOf(entities); //TODO*** examine and return only successfully stored entities
      }
      //TODO: rewrite to service exception with more elaborate error report of what went wrong possibly for each entity
      throw new RuntimeException(
          "Failed to save entity to Elasticsearch, status code is " + response.getStatusLine().getStatusCode()
      );
    } catch (IOException e) {
      //TODO: rewrite to service exception with more elaborate error report
      throw new RuntimeException("Failed to save entity to Elasticsearch", e);
    }
  }

  @Override
  public <S extends E> S save(S entity) {
    Collection<S> stored = this.save(Collections.singletonList(entity));
    if (stored.isEmpty()) {
      return null;
    }
    return stored.iterator().next();
  }

  @Override
  public <S extends E> void refresh(Collection<S> entities) {

  }

  @Override
  public int deleteById(Collection<? extends ID> ids) {
    for(ID id : ids) {
      deleteById(id);
    }
    return 1;
  }

  @Override
  public int deleteById(ID id) {
    try {
      Request request = new Request(
          "DELETE", "/" + getIndexName() + "/_doc/" + id.toString()
      );
      Response response = getElasticsearchClient().performRequest(request);
      return response.getStatusLine().getStatusCode() == 200 ? 1 : 0; //TODO: if non 200 wouldn't that be an error we'd like to report?
    } catch (IOException e) {
      //TODO: rewrite to service exception with more elaborate error report of what went wrong
      throw new RuntimeException("Failed to delete entity from Elasticsearch", e);
    }
  }

  @Override
  public <S extends E> List<S> delete(Collection<S> entities) {
    return List.of();
  }

  @Override
  public <S extends E> S delete(S entity) {
    return null;
  }

  @Override
  public List<E> findById(Collection<ID> ids) {
    return List.of();
  }

  @Override
  public E findById(ID id) {
    try {
      Request request = new Request("GET", "/" + getIndexName() + "/_doc/" + id.toString());
      Response response = getElasticsearchClient().performRequest(request);
      if (response.getStatusLine().getStatusCode() == 200) {
        String json = response.getEntity().getContent().toString();
        Map<String, Object> sourceMap = getObjectMapper().readValue(json, new TypeReference<>() {});
        //TODO: get a mapper as getElasticsearchClient and separate serialization and retrieval
        // (so they can both change/use/test independently)
        return convertMapToEntity(sourceMap);
      } else {
        return null;
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to retrieve entity from Elasticsearch", e);
    }
  }

  @Override
  public List<E> find(RepositoryExecContext<E> context) {
    return List.of();
  }

  @Override
  public <X extends E> List<X> find(Class<X> cls, RepositoryExecContext<X> context) {
    return List.of();
  }

  @Override
  public List<E> find() {
    return List.of();
  }

  //TODO: Query mapTo QueryNode mapTo String should be controlled by repository
  // therefore -> no public method should be allowed to invoke a ES query with a string!!!!!
  // (responsibility for converting from com.dropchop.recyclone.model.api.query.Query to String should be here)
  // to allow for extensions a set of callbacks should be provided via execution context.
  // think about on and after which steps should we callback (I didn't yet)
  public <S extends E> List<S> search(String query) {
    return search(query, 0, 10000);
  }

  public <S extends E> List<S> search(String query, int from, int size) {
    try {
      StringBuilder builder = new StringBuilder();
      builder.append(query);
      builder.insert(builder.length() - 1, ",\"size\":" + size + ",");

      Request request = new Request("GET", "/" + getIndexName() + "/_search");

      boolean hasMoreResults = true;
      List<S> results = new ArrayList<>();

      ObjectMapper mapper = getObjectMapper();
      RestClient restClient = getElasticsearchClient();
      while (hasMoreResults) {
        StringBuilder builder2 = new StringBuilder();
        builder2.append(builder);
        builder2.insert(builder.length() - 1, "\"from\":" + from);
        request.setJsonEntity(builder2.toString());

        Response response = restClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());

        ElasticSearchResult<S> searchResult = mapper.readValue(
          jsonResponse, new TypeReference<>() {}
        );

        List<S> hits = searchResult.getHits().getHits().stream()
          .map(ElasticSearchResult.Hit::getSource)
          .toList();

        results.addAll(hits);

        hasMoreResults = hits.size() == size;
        from += size;
      }

      return results;
    } catch (IOException e) {
      //TODO: rewrite to service exception with more elaborate error report of what went wrong
      throw new RuntimeException("Failed to search in Elasticsearch", e);
    }
  }

  protected abstract E convertMapToEntity(Map<String, Object> source);

  protected abstract ObjectMapper getObjectMapper();

  protected abstract RestClient getElasticsearchClient();
}
