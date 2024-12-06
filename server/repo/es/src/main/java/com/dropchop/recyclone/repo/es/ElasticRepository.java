package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.api.RepositoryExecContextListener;
import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.invoke.StatusMessage;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.utils.Strings;
import com.dropchop.recyclone.model.dto.invoke.QueryParams;
import com.dropchop.recyclone.repo.api.ElasticCrudRepository;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.api.listener.EntityQuerySearchResultListener;
import com.dropchop.recyclone.repo.api.listener.MapQuerySearchResultListener;
import com.dropchop.recyclone.repo.api.listener.QuerySearchResultListener;
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.dropchop.recyclone.repo.es.mapper.ElasticSearchResult;
import com.dropchop.recyclone.repo.es.mapper.QueryNodeObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@Slf4j
@SuppressWarnings("unused, unchecked")
public abstract class ElasticRepository<E, ID> implements ElasticCrudRepository<E, ID> {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  ExecContextContainer ctxContainer;

  protected abstract ElasticQueryMapper getElasticQueryMapper();

  protected abstract ObjectMapper getObjectMapper();

  protected abstract RestClient getElasticsearchClient();

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
    ElasticExecContext<E> context = new ElasticExecContext<E>().of(ctxContainer.get());
    for (CriteriaDecorator decorator : getCommonCriteriaDecorators()) {
      context.decorateWith(decorator);
    }
    return getRepositoryExecContext().totalCount(mappingContext);
  }

  protected String getIndexName() {
    return Strings.toSnakeCase(getRootClass().getSimpleName());
  }

  protected <S extends E> List<S> executeBulkRequest(Collection<S> entities, ElasticBulkMethod method) {
    if (entities == null || entities.isEmpty()) {
      return Collections.emptyList();
    }

    StringBuilder bulkRequestBody = new StringBuilder();
    ObjectMapper objectMapper = getObjectMapper();

    bulkRequestBody = method.buildBulkRequest(entities, bulkRequestBody, objectMapper);

    Request request = new Request("POST", "/_bulk");
    request.setJsonEntity(bulkRequestBody.toString());

    try {
      Response response = getElasticsearchClient().performRequest(request);

      return method.checkSuccessfulResponse(entities, response, objectMapper);

    } catch (ServiceException | IOException e) {
      throw new ServiceException(new StatusMessage(ErrorCode.unknown_error,
        "Failed to save entity to Elasticsearch", Set.of(new AttributeString("error", e.getMessage()))));
    }
  }

  @Override
  public <S extends E> List<S> save(Collection<S> entities) {
    ElasticBulkMethod saveBulkMethod = new ElasticBulkMethod(
      ElasticBulkMethod.MethodType.INDEX,
      getIndexName());

    return executeBulkRequest(entities, saveBulkMethod);
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
      Request request = new Request("DELETE", "/" + getIndexName() + "/_doc/" + id);
      Response response = getElasticsearchClient().performRequest(request);

      if (response.getStatusLine().getStatusCode() == 200) {
        return 1;
      } else {
        int statusCode = response.getStatusLine().getStatusCode();
        throw  new ServiceException(
          ErrorCode.data_error,
          "Failed to delete entity with ID: " + id + ". Status code: " + statusCode,
          Set.of(new AttributeString("status", String.valueOf(statusCode)))
        );
      }
    } catch (IOException e) {
      throw new ServiceException(
        ErrorCode.data_error,
        "Error occurred during deletion of entity with ID: " + id,
        Set.of(new AttributeString("ID", id.toString())),
        e
      );
    }
  }

  @Override
  public <S extends E> List<S> delete(Collection<S> entities) {
    ElasticBulkMethod elasticDeleteMethod = new ElasticBulkMethod(
      ElasticBulkMethod.MethodType.DELETE,
      getIndexName()
    );

    return executeBulkRequest(entities, elasticDeleteMethod);
  }

  @Override
  public <S extends E> S delete(S entity) {
    return delete(List.of(entity)).getFirst();
  }

  @Override
  public List<E> findById(Collection<ID> ids) {
    List<E> findings = new ArrayList<>();
    for (ID id : ids) {
      findings.add(this.findById(id));
    }
    return findings;
  }

  @Override
  public E findById(ID id) {
    try {
      Request request = new Request("GET", "/" + getIndexName() + "/_doc/" + id.toString());
      Response response = getElasticsearchClient().performRequest(request);
      E result = null;

      if (response.getStatusLine().getStatusCode() == 200) {
        String json = EntityUtils.toString(response.getEntity());
        ElasticSearchResult<E> searchResult = getObjectMapper().readValue(json, new TypeReference<>() {});
        List<ElasticSearchResult.Hit<E>> results = searchResult.getHits().getHits();

        for(ElasticSearchResult.Hit<E> hit : results) {
          result = hit.getSource();
        }

        return result;
      } else if (response.getStatusLine().getStatusCode() == 404) {
        return null;
      } else {
        throw new ServiceException(
          ErrorCode.data_error,
          "Unexpected response status: " + response.getStatusLine().getStatusCode(),
          Set.of(new AttributeString("status", String.valueOf(response.getStatusLine().getStatusCode())))
        );
      }
    } catch (IOException e) {
      throw new ServiceException(
        ErrorCode.data_error,
        "Failed to retrieve entity with ID: " + id,
        Set.of(new AttributeString("ID", id.toString())),
        e
      );
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

  private <S extends E> void initializeContext(ElasticExecContext<S> context, QueryParams params) {
    context.init((Class<S>) getRootClass(), Strings.toSnakeCase(getRootClass().getSimpleName()), params);
  }

  private void applyDecorators(RepositoryExecContext<?> context) {
    context.getListeners().stream()
        .filter(listener -> listener instanceof CriteriaDecorator)
        .map(listener -> (CriteriaDecorator) listener)
        .forEach(CriteriaDecorator::decorate);
  }

  private QueryNodeObject buildSortOrder(List<String> sortList, Class<?> rootClass) {
    QueryNodeObject sortOrder = new QueryNodeObject();
    if (!sortList.isEmpty()) {
      List<QueryNodeObject> sortEntries = sortList.stream()
          .map(sort -> {
            QueryNodeObject sortEntry = new QueryNodeObject();
            sortEntry.put(sort, "desc");
            return sortEntry;
          })
          .toList();
      sortOrder.put("sort", sortEntries);
    } else {
      QueryNodeObject defaultSort = new QueryNodeObject();
      if(HasCreated.class.isAssignableFrom(rootClass)) {
        defaultSort.put("created", "desc");
        sortOrder.put("sort", defaultSort);
      } else if(HasUuid.class.isAssignableFrom(rootClass)) {
        defaultSort.put("uuid.keyword", "desc");
        sortOrder.put("sort", defaultSort);
      } else if(HasCode.class.isAssignableFrom(rootClass)) {
        defaultSort.put("code.keyword", "desc");
        sortOrder.put("sort", defaultSort);
      } else {
        throw new ServiceException(
            ErrorCode.internal_error,
            "No Id or Code set to sort by; for Es deep pagination!"
        );
      }
    }
    return sortOrder;
  }

  private QueryNodeObject buildQueryObject(RepositoryExecContext<?> context,
                                           List<Object> searchAfterValues,
                                           QueryNodeObject sortOrder,
                                           int size, int from) {
    ElasticQueryMapper esQueryMapper = new ElasticQueryMapper();
    QueryNodeObject queryObject = esQueryMapper.mapToString(context.getParams());
    queryObject.put("size", Math.min(size, 10000));

    if (searchAfterValues == null) queryObject.put("from", from);
    queryObject.putAll(sortOrder);
    if (searchAfterValues != null) queryObject.put("search_after", searchAfterValues);

    return queryObject;
  }

  private <S extends E> List<ElasticSearchResult.Hit<S>> executeSearchAndExtractHits(
      QueryNodeObject queryObject,
      ElasticExecContext<S> context) throws IOException {
    String query = getObjectMapper().writeValueAsString(queryObject);
    Request request = new Request("GET", "/" + getIndexName() + "/_search");
    request.setJsonEntity(query);

    String response = EntityUtils.toString(getElasticsearchClient().performRequest(request).getEntity());
    ElasticSearchResult<S> searchResult = getObjectMapper().readValue(response, new TypeReference<>() {});

    return searchResult.getHits().getHits();
  }

  private <S> List<Object> getSearchAfterValues(List<ElasticSearchResult.Hit<S>> hits) {
    return hits.isEmpty() ? null : hits.getLast().getSort();
  }

  @Override
  public <S extends E> List<S> search(QueryParams params, RepositoryExecContext<S> context) {
    if (!(context instanceof ElasticExecContext<S> elasticContext)) {
      throw new ServiceException(
        ErrorCode.parameter_validation_error,
        "Invalid context: Expected ElasticExecContext but received " + context.getClass()
      );
    }

    initializeContext(elasticContext, params);
    applyDecorators(context);

    List<S> allHits = new ArrayList<>();
    List<Object> searchAfterValues = null;
    int size = context.getParams().tryGetResultFilter().size();
    int from = context.getParams().tryGetResultFilter().from();
    QueryNodeObject sortOrder = buildSortOrder(
        context.getParams().tryGetResultFilter().sort(), elasticContext.getRootClass()
    );

    RepositoryExecContextListener listener = context.getListeners().stream()
      .filter(l -> l instanceof QuerySearchResultListener).findFirst().orElse(null);

    boolean hasMoreHits = true;
    while (hasMoreHits) {
      try {
        QueryNodeObject queryObject = buildQueryObject(context, searchAfterValues, sortOrder, size, from);

        if(listener instanceof EntityQuerySearchResultListener) {
          //((EntityQuerySearchResultListener) queryListener).onResult();
        } else if(listener instanceof MapQuerySearchResultListener) {
          List<ElasticSearchResult.Hit<S>> hits = executeSearchAndExtractHits(queryObject, elasticContext);

          if (hits.isEmpty()) {
            hasMoreHits = false;
          } else {
            searchAfterValues = getSearchAfterValues(hits);
            for(ElasticSearchResult.Hit<S> hit : hits) {
              S result = hit.getSource();
              allHits.add(result);

              ((MapQuerySearchResultListener) listener).onResult(result);
            }
          }
        } else {
          throw new ServiceException(
            ErrorCode.process_error,
            "No query listener is set!"
          );
        }
      } catch (ServiceException e) {
        throw new ServiceException(
          ErrorCode.data_error,
          "Failed to execute search query.",
          Set.of(new AttributeString("errorMessage", e.getMessage())),
          e
        );
      } catch (Exception e) {
        throw new ServiceException(
          ErrorCode.internal_error,
          "Unexpected error occurred during search execution.",
          Set.of(new AttributeString("errorMessage", e.getMessage())),
          e
        );
      }
    }

    return allHits;
  }
}
