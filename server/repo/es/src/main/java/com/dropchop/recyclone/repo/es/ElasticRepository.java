package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.utils.Strings;
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.es.mapper.ElasticSearchResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

import jakarta.annotation.PostConstruct;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@SuppressWarnings("unused")
public abstract class ElasticRepository<E, ID> implements CrudRepository<E, ID> {

  private RestClient elasticsearchClient;
  private ObjectMapper objectMapper;
  private Class<E> rootClass;

  @PostConstruct
  public void init() {
    this.elasticsearchClient = getElasticsearchClient();
    this.objectMapper = getObjectMapper();
    this.rootClass = getRootClass();
  }

  @Override
  public Class<E> getRootClass() {
    return rootClass;
  }

  @Override
  public RepositoryExecContext<E> getRepositoryExecContext() {
    return null;
  }

  @Override
  public RepositoryExecContext<E> getRepositoryExecContext(MappingContext mappingContext) {
    return null;
  }

  @Override
  public <S extends E> List<S> save(Collection<S> entities) {
    if (entities == null || entities.isEmpty()) {
      return Collections.emptyList();
    }

    StringBuilder bulkRequestBody = new StringBuilder();

    try {
      for (S entity : entities) {
        bulkRequestBody.append("{ \"index\" : { \"_index\" : \"your_index_name\", \"_id\" : \"")
          .append(UUID.randomUUID())
          .append("\" } }\n");
        bulkRequestBody.append(this.objectMapper.writeValueAsString(entity)).append("\n");
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to serialize entity to JSON", e);
    }

    Request request = new Request("POST", "/_bulk");
    request.setJsonEntity(bulkRequestBody.toString());

    try {
      Response response = elasticsearchClient.performRequest(request);

      if(response.getStatusLine().getStatusCode() == 200) {
        return List.copyOf(entities);
      }

      throw new RuntimeException("Failed to save entity to Elasticsearch, status code is " + response.getStatusLine().getStatusCode());
    } catch (IOException e) {
      throw new RuntimeException("Failed to save entity to Elasticsearch", e);
    }
  }

  @Override
  public <S extends E> S save(S entity) {
    try {
      String id = getEntityId(entity);

      Request request = new Request("PUT", "/" + Strings.toSnakeCase(rootClass.getSimpleName()) + "/_doc/" + id);
      if(entity instanceof String) {
        request.setJsonEntity((String) entity);
      }

      Response response = elasticsearchClient.performRequest(request);
      System.out.println("Indexed document: " + response.getStatusLine());

      return entity;
    } catch (IOException e) {
      throw new RuntimeException("Failed to save entity to Elasticsearch", e);
    }
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
      Request request = new Request("DELETE", "/" + Strings.toSnakeCase(rootClass.getSimpleName()) + "/_doc/" + id.toString());
      Response response = elasticsearchClient.performRequest(request);
      return response.getStatusLine().getStatusCode() == 200 ? 1 : 0;
    } catch (IOException e) {
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
      Request request = new Request("GET", "/" + Strings.toSnakeCase(rootClass.getSimpleName()) + "/_doc/" + id.toString());
      Response response = elasticsearchClient.performRequest(request);

      if (response.getStatusLine().getStatusCode() == 200) {
        String json = response.getEntity().getContent().toString();
        Map<String, Object> sourceMap = objectMapper.readValue(json, new TypeReference<>() {});
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

  public <S extends E> List<S> search(String query) {
    return search(query, 0, 10000);
  }

  public <S extends E> List<S> search(String query, int from, int size) {
    try {
      StringBuilder builder = new StringBuilder();
      builder.append(query);
      builder.insert(builder.length() - 1, ",\"size\":" + size + ",");

      Request request = new Request("GET", "/" + Strings.toSnakeCase(rootClass.getSimpleName()) + "/_search");

      boolean hasMoreResults = true;
      List<S> results = new ArrayList<>();

      while (hasMoreResults) {
        StringBuilder builder2 = new StringBuilder();
        builder2.append(builder);
        builder2.insert(builder.length() - 1, "\"from\":" + from);
        request.setJsonEntity(builder2.toString());

        Response response = elasticsearchClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());

        ElasticSearchResult<S> searchResult = this.objectMapper.readValue(
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
      throw new RuntimeException("Failed to search in Elasticsearch", e);
    }
  }

  protected abstract E convertMapToEntity(Map<String, Object> source);

  protected abstract String getEntityId(E entity);

  protected abstract ObjectMapper getObjectMapper();

  protected abstract RestClient getElasticsearchClient();

  public void close() throws IOException {
    this.elasticsearchClient.close();
  }
}
