package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.api.model.attr.AttributeDecimal;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.repo.ElasticCrudRepository;
import com.dropchop.recyclone.base.api.repo.config.*;
import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;
import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.base.EsEntity;
import com.dropchop.recyclone.base.es.repo.listener.AggregationResultListener;
import com.dropchop.recyclone.base.es.repo.listener.MapResultListener;
import com.dropchop.recyclone.base.es.repo.listener.QueryResultListener;
import com.dropchop.recyclone.base.es.repo.mapper.ElasticQueryMapper;
import com.dropchop.recyclone.base.es.repo.mapper.ElasticSearchResult;
import com.dropchop.recyclone.base.es.repo.mapper.ElasticSearchResult.Container;
import com.dropchop.recyclone.base.es.repo.mapper.ElasticSearchResult.Hit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.*;

import static com.dropchop.recyclone.base.api.model.query.Condition.and;
import static com.dropchop.recyclone.base.api.model.query.Condition.field;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.in;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class ElasticRepository<E extends EsEntity, ID> implements ElasticCrudRepository<E, ID> {

  private static final String HTTP_POST = "POST";
  private static final String ENDPOINT_SEARCH = "/_search";
  private static final String ENDPOINT_DELETE_BY_QUERY = "/_delete_by_query";

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  protected ExecContextContainer ctxContainer;

  public abstract ElasticQueryMapper getElasticQueryMapper();

  public abstract ObjectMapper getObjectMapper();

  public abstract RestClient getElasticsearchClient();

  public String getClassAlias(Class<?> cls) {
    return cls.getSimpleName().toLowerCase();
  }

  public ElasticIndexConfig getElasticIndexConfig() {
    return DefaultIndexConfig.builder().rootClass(getRootClass()).build();
  }

  protected Collection<CriteriaDecorator> getCommonCriteriaDecorators() {
    return List.of(
      new PageCriteriaDecorator()
    );
  }

  @Override
  public ElasticExecContext<E> getRepositoryExecContext() {
    ElasticExecContext<E> context = new ElasticExecContext<E>().of(ctxContainer.get());
    String alias = getClassAlias(this.getRootClass());
    for (CriteriaDecorator decorator : getCommonCriteriaDecorators()) {
      context.decorateWith(decorator);
    }
    context.init(this.getRootClass(), alias);
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

  private Response invokeBulkRequest(BulkRequestBuilder bulkRequestBuilder, Collection<?> entities) {
    ObjectMapper mapper = getObjectMapper();
    Request request = bulkRequestBuilder.bulkRequest(entities);
    Response response;
    try {
      response = getElasticsearchClient().performRequest(request);
    } catch (ServiceException | IOException e) {
      throw new ServiceException(
          ErrorCode.unknown_error, "Failed to save entity to Elasticsearch",
          Set.of(new AttributeString("error", e.getMessage())), e
      );
    }
    return response;
  }

  @Override
  public <S extends E> List<S> save(Collection<S> entities) {
    ObjectMapper mapper = getObjectMapper();
    BulkRequestBuilder bulkRequestBuilder = new BulkRequestBuilder(
        BulkRequestBuilder.MethodType.INDEX, mapper, getElasticIndexConfig()
    );
    Response response = invokeBulkRequest(bulkRequestBuilder, entities);
    BulkResponseParser responseParser = new BulkResponseParser(mapper);
    return responseParser.parseResponse(entities, response);
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
  public <X extends ID> int deleteById(Collection<X> ids) {
    if (ids == null || ids.isEmpty()) {
      return 0;
    }
    ObjectMapper mapper = getObjectMapper();
    BulkRequestBuilder bulkRequestBuilder = new BulkRequestBuilder(
        BulkRequestBuilder.MethodType.DELETE, mapper, getElasticIndexConfig()
    );
    Response response = invokeBulkRequest(bulkRequestBuilder, ids);
    BulkResponseParser responseParser = new BulkResponseParser(mapper);
    return responseParser.parseResponse(ids, response).size();
  }

  @Override
  public <X extends ID> int deleteById(X id) {
    return deleteById(Collections.singletonList(id));
  }

  @Override
  public <S extends E> List<S> delete(Collection<S> entities) {
    ObjectMapper mapper = getObjectMapper();
    BulkRequestBuilder bulkRequestBuilder = new BulkRequestBuilder(
        BulkRequestBuilder.MethodType.DELETE, mapper, getElasticIndexConfig()
    );
    Response response = invokeBulkRequest(bulkRequestBuilder, entities);
    BulkResponseParser responseParser = new BulkResponseParser(mapper);
    return responseParser.parseResponse(entities, response);
  }

  @Override
  public <S extends E> S delete(S entity) {
    return delete(List.of(entity)).getFirst();
  }

  protected Request buildRequestForSearch(QueryNodeObject query, String endpoint) {
    ElasticIndexConfig indexConfig = this.getElasticIndexConfig();
    String indexName;
    if (indexConfig instanceof HasQueryBasedReadIndex hasQueryBasedReadIndex) {
      indexName = hasQueryBasedReadIndex.getReadIndexName(query);
      return new Request(HTTP_POST, "/" + indexName + endpoint);
    }
    if (indexConfig instanceof HasRootAlias hasRootAlias) {
      indexName = hasRootAlias.getRootAlias();
      return new Request(HTTP_POST, "/" + indexName + endpoint);
    }
    if (indexConfig instanceof HasStaticReadIndex hasStaticReadIndex) {
      indexName = hasStaticReadIndex.getReadIndex();
      return new Request(HTTP_POST, "/" + indexName + endpoint);
    }
    throw new ServiceException(ErrorCode.internal_error, "No valid index config found!");
  }

  @Override
  public <S extends E> int deleteByQuery(RepositoryExecContext<S> context) {
    QueryParams params = context.getParams();
    QueryNodeObject queryObject = getElasticQueryMapper().mapToString(params);
    String query;

    try {
      query = getObjectMapper().writeValueAsString(queryObject);
    } catch (JsonProcessingException e) {
      throw new ServiceException(
        ErrorCode.internal_error, "Unable to serialize QueryNodeObject", e
      );
    }

    try {
      Request request = this.buildRequestForSearch(queryObject, ENDPOINT_DELETE_BY_QUERY);
      request.setJsonEntity(query);
      Response response = getElasticsearchClient().performRequest(request);
      if (response.getStatusLine().getStatusCode() == 200) {
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonNode jsonResponse = getObjectMapper().readTree(responseBody);
        return jsonResponse.get("deleted").asInt();
      } else if (response.getStatusLine().getStatusCode() == 404) {
        throw new ServiceException(
          ErrorCode.data_missing_error, "Missing data for query",
          Set.of(
            new AttributeString("status", String.valueOf(response.getStatusLine().getStatusCode())),
            new AttributeString("delete query", query)
          )
        );
      } else {
        throw new ServiceException(
          ErrorCode.data_error, "Unexpected response status: " + response.getStatusLine().getStatusCode(),
          Set.of(
            new AttributeDecimal("status", response.getStatusLine().getStatusCode()),
            new AttributeString("query", query)
          )
        );
      }
    } catch (IOException e) {
      throw new ServiceException(
          ErrorCode.internal_error,
          "Unable to execute delete query",
          Set.of(new AttributeString("delete query", query)), e
      );
    }
  }

  protected QueryNodeObject buildSortOrder(List<String> sortList, ElasticIndexConfig indexConfig) {
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
      return sortOrder;
    } else if (indexConfig instanceof HasClassBasedDefaultSort hasClassBasedDefaultSort) {
      QueryNodeObject defaultSort = hasClassBasedDefaultSort.getSortOrder();
      if (defaultSort == null) {
        throw new ServiceException(
            ErrorCode.internal_error, "No sort order received from index config for deep pagination!"
        );
      }
      sortOrder.put("sort", defaultSort);
      return sortOrder;
    }
    return null;
  }

  protected <S extends E> QueryNodeObject buildQueryObject(QueryParams queryParams, List<Object> searchAfterValues) {
    ElasticIndexConfig indexConfig = this.getElasticIndexConfig();
    QueryNodeObject sortOrder = buildSortOrder(
        queryParams.tryGetResultFilter().sort(), indexConfig
    );

    int sizeOfPagination = indexConfig.getSizeOfPagination();
    int maxSize = queryParams.tryGetResultFilter().size();
    int from = queryParams.tryGetResultFilter().from();

    ElasticQueryMapper esQueryMapper = new ElasticQueryMapper();

    QueryNodeObject initialQueryObject = esQueryMapper.mapToString(queryParams);
    int effectiveSize = maxSize > 0
      ? Math.min(maxSize, 10000)
      : Math.min(sizeOfPagination, 10000);
    initialQueryObject.put("size", effectiveSize);

    if (searchAfterValues == null) {
      initialQueryObject.put("from", from);
    }
    if (sortOrder != null) {
      initialQueryObject.putAll(sortOrder);
    }
    if (searchAfterValues != null) {
      initialQueryObject.put("search_after", searchAfterValues);
    }

    return initialQueryObject;
  }

  private <X> void executeQuery(QueryNodeObject queryObject, boolean skipParsing,
                                Collection<RepositoryExecContextListener> listeners) {
    ProfileTimer timer = new ProfileTimer();
    ObjectMapper objectMapper = getObjectMapper();
    String query;
    try {
      query = objectMapper.writeValueAsString(queryObject);
    } catch (IOException e) {
      throw new ServiceException(ErrorCode.internal_error, "Unable to serialize QueryNodeObject", e);
    }
    Class<E> cls = getRootClass();
    try {
      Request request = this.buildRequestForSearch(queryObject, ENDPOINT_SEARCH);

      request.setJsonEntity(query);
      Response response = getElasticsearchClient().performRequest(request);
      if (response.getStatusLine().getStatusCode() == 200) {
        String responseBody = EntityUtils.toString(response.getEntity());
        ElasticSearchResult<X> searchResult;
        if (skipParsing) {
          searchResult = objectMapper.readValue(
              responseBody, new TypeReference<>() {}
          );
        } else {
          searchResult = objectMapper.readValue(
            responseBody, TypeFactory.defaultInstance().constructParametricType(
              ElasticSearchResult.class, cls
            )
          );
        }
        int count = 0;

        for (Hit<X> hit : searchResult.getHits().getHits()) {
          count++;
          for (RepositoryExecContextListener listener : listeners) {
            if(listener instanceof QueryResultListener<?>) {
              @SuppressWarnings("unchecked")
              QueryResultListener<X> queryResultListener = (QueryResultListener<X>) listener;
              queryResultListener.onResult(hit);
            }
          }
        }

        if (searchResult.getAggregations() != null) {
          searchResult.getAggregations().forEach((name, agg) ->
            listeners.stream()
              .filter(AggregationResultListener.class::isInstance)
              .forEach(listener ->
                ((AggregationResultListener) listener).onAggregation(name, agg)
              )
          );
        }
        log.debug("Executed query [{}] with [{}] result size in [{}]ms.", query, count, timer.stop());
      } else if (response.getStatusLine().getStatusCode() == 404) {
        throw new ServiceException(
          ErrorCode.data_missing_error, "Missing data for query",
          Set.of(
            new AttributeString("status", String.valueOf(response.getStatusLine().getStatusCode())),
            new AttributeString("query", query)
          )
        );
      } else {
        throw new ServiceException(
          ErrorCode.data_error, "Unexpected response status: " + response.getStatusLine().getStatusCode(),
          Set.of(
            new AttributeString("status", String.valueOf(response.getStatusLine().getStatusCode())),
            new AttributeString("query", query)
          )
        );
      }
    } catch (IOException e) {
      throw new ServiceException(
        ErrorCode.internal_error, "Unable to execute query",
        Set.of(new AttributeString("query", query)), e
      );
    }
  }

  private void applyDecorators(RepositoryExecContext<?> context) {
    context.getListeners().stream()
      .filter(listener -> listener instanceof CriteriaDecorator)
      .map(listener -> (CriteriaDecorator) listener)
      .forEach(CriteriaDecorator::decorate);
  }

  private <S> List<Object> getSearchAfterValues(List<Hit<S>> hits) {
    return hits.isEmpty() ? null : hits.getLast().getSort();
  }

  public <S extends E> List<S> search(RepositoryExecContext<S> context) {
    if (!(context instanceof ElasticExecContext<S> elasticContext)) {
      throw new ServiceException(
        ErrorCode.parameter_validation_error,
        "Invalid context: Expected ElasticExecContext but received " + context.getClass()
      );
    }

    applyDecorators(context);

    RepositoryExecContextListener listener = context.getListeners().stream()
      .filter(l -> l instanceof MapResultListener).findFirst().orElse(null);

    RepositoryExecContextListener aggregationListener = context.getListeners().stream()
      .filter(l -> l instanceof AggregationResultListener).findFirst().orElse(null);
    if (listener == null && elasticContext.isSkipObjectParsing()) {
      throw new ServiceException(
        ErrorCode.internal_error,
        "Skip object parsing was enabled but there is no raw result listener. Such implementation makes no sense!"
      );
    }

    QueryParams queryParams = context.getParams();
    int maxSize = queryParams.tryGetResultFilter().getSize();

    boolean hasMoreHits = true;
    List<S> results = new ArrayList<>();

    List<Object> searchAfterValues = null;
    while (hasMoreHits && results.size() < maxSize) {
      try {
        QueryNodeObject queryObject = buildQueryObject(queryParams, searchAfterValues);
        Container<Hit<S>> last = new Container<>();
        if (((ElasticExecContext<S>) context).isSkipObjectParsing() && listener != null) {
          executeQuery(queryObject, true, List.of(
            (QueryResultListener<S>) last::setHit
          ));
        } else {
          if (aggregationListener != null) {
            executeQuery(queryObject, false, List.of(
              (QueryResultListener<S>) hit -> {
                results.add(hit.getSource());
                last.setHit(hit);
              },
              aggregationListener
            ));
          } else {
            executeQuery(queryObject, false, List.of(
              (QueryResultListener<S>) hit -> {
                results.add(hit.getSource());
                last.setHit(hit);
              }
            ));
          }
        }
        if (last.isEmpty()) {
          hasMoreHits = false;
        } else {
          searchAfterValues = last.getHit().getSort();
        }
      } catch (ServiceException e) {
        throw new ServiceException(
          ErrorCode.data_error, "Failed to execute search query.",
          Set.of(new AttributeString("errorMessage", e.getMessage())), e
        );
      } catch (Exception e) {
        throw new ServiceException(
          ErrorCode.internal_error, "Unexpected error occurred during search execution.",
          Set.of(new AttributeString("errorMessage", e.getMessage())), e
        );
      }
    }

    return results;
  }

  @Override
  public <S extends E, X extends ID> List<S> findById(Collection<X> ids) {
    Collection<String> strIds = ids.stream().map(Object::toString).toList();
    Class<E> cls = getRootClass();
    QueryParams params = new QueryParams();
    QueryNodeObject queryObject;
    if (!ids.isEmpty()) {
      if (HasCode.class.isAssignableFrom(cls)) {
        params.condition(and(field("code", in(strIds))));
      } else if (HasUuid.class.isAssignableFrom(cls)) {
        params.condition(and(field("uuid", in(strIds))));
      }
    }
    queryObject = buildQueryObject(params, null);
    List<S> results = new ArrayList<>();
    executeQuery(queryObject, false, List.of(
        (QueryResultListener<S>) result -> results.add(result.getSource())
    ));
    return results;
  }

  @Override
  public <S extends E, X extends ID> S findById(X id) {
    List<S> entities = findById(List.of(id));
    return entities.isEmpty() ? null : entities.getFirst();
  }

  @Override
  public <S extends E> List<S> find(RepositoryExecContext<S> context) {
    if (!(context instanceof ElasticExecContext<S> elasticContext)) {
      throw new ServiceException(
          ErrorCode.parameter_validation_error,
          "Invalid context: Expected ElasticExecContext but received " + context.getClass()
      );
    }
    return this.search(elasticContext);
  }

  @Override
  public <S extends E> List<S> find(Class<S> cls, RepositoryExecContext<S> context) {
    throw new ServiceException(
        ErrorCode.internal_error,
        "Unimplemented"
    );
  }

  @Override
  public <S extends E> List<S> find() {
    throw new ServiceException(
        ErrorCode.internal_error,
        "Unimplemented"
    );
  }
}
