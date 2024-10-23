package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.*;

import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
public abstract class ElasticRepository<E, ID> implements CrudRepository<E, ID> {

  @Setter
  private ObjectMapper objectMapper = new ObjectMapper();
  private final RestClient elasticsearchClient;
  private String indexName = null;

  protected ElasticRepository() {
    this.elasticsearchClient = RestClient.builder(new HttpHost("localhost", 9300, "http")).build();
  }

  public String getClassAlias(Class<?> cls) {
    return cls.getSimpleName().toLowerCase();
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

      Request request = new Request("PUT", "/" + indexName + "/_doc/" + id);
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
      Request request = new Request("DELETE", "/" + indexName + "/_doc/" + id.toString());
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
      Request request = new Request("GET", "/" + indexName + "/_doc/" + id.toString());
      Response response = elasticsearchClient.performRequest(request);

      if (response.getStatusLine().getStatusCode() == 200) {
        String json = response.getEntity().getContent().toString();
        Map<String, Object> sourceMap = objectMapper.readValue(json, Map.class);
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

  public Response search(String query) {
    try {
      this.indexName = getRootClass().getSimpleName().toLowerCase();
      Request request = new Request("GET", "/" + indexName + "/_search");
      request.setJsonEntity(query);

      return elasticsearchClient.performRequest(request);
    } catch (IOException e) {
      throw new RuntimeException("Failed to search in Elasticsearch", e);
    }
  }

  protected abstract Map<String, Object> convertEntityToMap(E entity);

  protected abstract E convertMapToEntity(Map<String, Object> source);

  protected abstract String getEntityId(E entity);

  public void close() throws IOException {
    this.elasticsearchClient.close();
  }
}
