package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.api.mapper.TotalCountExecContextListener;
import com.dropchop.recyclone.base.api.model.attr.AttributeDecimal;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.*;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.base.EsEntity;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import com.dropchop.recyclone.base.es.repo.QueryResponseParser.SearchResultMetadata;
import com.dropchop.recyclone.base.es.repo.config.*;
import com.dropchop.recyclone.base.es.repo.listener.AggregationResultListener;
import com.dropchop.recyclone.base.es.repo.listener.QueryResultListener;
import com.dropchop.recyclone.base.es.repo.marker.BlockAllDelete;
import com.dropchop.recyclone.base.es.repo.query.ElasticQueryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  public abstract ElasticQueryBuilder getElasticQueryBuilder();

  public abstract ObjectMapper getObjectMapper();

  public abstract RestClient getElasticsearchClient();

  public String getClassAlias(Class<?> cls) {
    return cls.getSimpleName().toLowerCase();
  }

  public ElasticIndexConfig getElasticIndexConfig() {
    return DefaultIndexConfig.builder().rootClass(getRootClass()).build();
  }

  public <S extends E> ElasticExecContext<S> createRepositoryExecContext() {
    Class<S> cls = getRootClass();
    String alias = getClassAlias(cls);
    return new ElasticExecContext<>(cls, alias);
  }

  protected <S extends E> Collection<ElasticCriteriaDecorator<S>> getCommonCriteriaDecorators() {
    // TODO: implement page criteria decorators
    return new ArrayList<>();
  }

  @Override
  public <S extends E> ElasticExecContext<S> getRepositoryExecContext() {
    ElasticExecContext<S> context = createRepositoryExecContext();
    context.of(ctxContainer.get());
    Collection<ElasticCriteriaDecorator<S>> decorators = getCommonCriteriaDecorators();
    for (ElasticCriteriaDecorator<S> decorator : decorators) {
      context.decorateWith(decorator);
    }
    return context;
  }

  @Override
  public <S extends E> RepositoryExecContext<S> getRepositoryExecContext(MappingContext mappingContext) {
    RepositoryExecContext<S> context = getRepositoryExecContext();
    return context.totalCount(mappingContext);
  }

  private Response invokeBulkRequest(BulkRequestBuilder bulkRequestBuilder, Collection<?> entities) {
    ObjectMapper mapper = getObjectMapper();
    String refresh = null;
    ExecContext<?> context = ctxContainer.get();
    if (context != null) {
      Params params = context.tryGetParams();
      if (params != null) {
        List<String> policy = params.getModifyPolicy();
        if (policy != null && policy.contains(Constants.ModifyPolicy.WAIT_FOR)) {
          refresh = "wait_for";
        }
      }
    }
    Request request = bulkRequestBuilder.bulkRequest(entities, refresh);
    if (refresh != null) {
      log.debug(
          "Will wait for operation [{}@{}] to be refreshed.",
          bulkRequestBuilder.getMethodType(),
          request.getEndpoint()
      );
    }
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

  private void throwDeleteException() {
    throw new ServiceException(
      ErrorCode.internal_error,
      "Delete operations are blocked by our implementation for repository: " + this.getClass().getSimpleName()
    );
  }

  @Override
  public <X extends ID> int deleteById(Collection<X> ids) {
    if (this instanceof BlockAllDelete) {
      this.throwDeleteException();
    }

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
    if (this instanceof BlockAllDelete) {
      this.throwDeleteException();
    }

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
    if (this instanceof BlockAllDelete) {
      this.throwDeleteException();
    }

    QueryParams params = context.getParams();
    QueryNodeObject queryObject = getElasticQueryBuilder().build(params, this);
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

  protected boolean useSearchAfterMode(QueryParams queryParams) {
    int requestSize = queryParams.tryGetResultFilter().getSize();
    int requestFrom = queryParams.tryGetResultFilter().getFrom();
    int maxSize = getElasticIndexConfig().getSizeOfPagination();
    return requestSize + requestFrom >= maxSize;  // we would overflow allowed elastic maximum
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

  protected <S extends E> QueryNodeObject buildQueryObject(QueryParams queryParams,
                                                           boolean useSearchAfterMode) {
    ElasticIndexConfig indexConfig = this.getElasticIndexConfig();
    QueryNodeObject sortOrder = buildSortOrder(
        queryParams.tryGetResultFilter().sort(), indexConfig
    );

    int sizeOfPagination = indexConfig.getSizeOfPagination();
    int size = queryParams.tryGetResultFilter().size();
    int from = queryParams.tryGetResultFilter().from();

    ElasticQueryBuilder esQueryMapper = new ElasticQueryBuilder();
    QueryNodeObject initialQueryObject = esQueryMapper.build(queryParams, this);

    if (useSearchAfterMode) {
      if (sortOrder == null) {
        throw new ServiceException(ErrorCode.internal_error, "Sort must be defined when using search after mode!");
      }
      initialQueryObject.put("size", size);
    } else {
      initialQueryObject.put("from", from);
      initialQueryObject.put("size", size);
    }

    if (sortOrder != null) {
      initialQueryObject.putAll(sortOrder);
    }

    return initialQueryObject;
  }

  protected <X> SearchResultMetadata executeSearch(QueryNodeObject queryObject,
                                                   Class<X> resultClass,
                                                   List<QueryResultListener<X>> queryListeners,
                                                   List<AggregationResultListener> aggListeners,
                                                   boolean searchAfterMode) {
    ProfileTimer timer = new ProfileTimer();
    ObjectMapper objectMapper = getObjectMapper();
    String query;
    try {
      query = objectMapper.writeValueAsString(queryObject);
    } catch (IOException e) {
      throw new ServiceException(ErrorCode.internal_error, "Unable to serialize QueryNodeObject", e);
    }
    //Class<E> cls = getRootClass();
    Request request = null;
    try {
      request = this.buildRequestForSearch(queryObject, ENDPOINT_SEARCH);
      request.setJsonEntity(query);
      Response response = getElasticsearchClient().performRequest(request);
      int queryId = query.hashCode();
      log.debug("Received response for query [{}][{}] in [{}]ms.", queryId, query, timer.stop());
      if (response.getStatusLine().getStatusCode() == 200) {
        QueryResponseParser parser = new QueryResponseParser(objectMapper, searchAfterMode);
        SearchResultMetadata searchResultMetadata;
        searchResultMetadata = parser.parse(
            response.getEntity().getContent(),
            resultClass,
            queryListeners,
            aggListeners
        );
        return searchResultMetadata;
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
      log.error("Error executing search on endpoint {} with query {}", request != null ? request.getEndpoint() : null,  query,  e);
      throw new ServiceException(
          ErrorCode.internal_error, "Unable to execute query",
          Set.of(new AttributeString("query", query)), e
      );
    }
  }

  public <S extends E> List<S> search(RepositoryExecContext<S> context) {
    if (!(context instanceof ElasticExecContext<S> elasticContext)) {
      throw new ServiceException(
        ErrorCode.parameter_validation_error,
        "Invalid context: Expected ElasticExecContext but received " + context.getClass()
      );
    }

    // collect result listeners and aggregators from context
    Class<S> cls = elasticContext.getRootClass();
    List<QueryResultListener<S>> queryListeners = new ArrayList<>();
    List<AggregationResultListener> aggListeners = new ArrayList<>();
    for (RepositoryExecContextListener listener : context.getListeners()) {
      if (listener instanceof QueryResultListener<?> queryResultListener) {
        //noinspection unchecked
        queryListeners.add((QueryResultListener<S>) queryResultListener);
      }
      if (listener instanceof AggregationResultListener aggregationResultListener) {
        aggListeners.add(aggregationResultListener);
      }
    }

    List<S> results = new ArrayList<>();
    if (queryListeners.isEmpty()) {
      // we add a default listener if no listeners were provided so we can return results
      queryListeners.add(
          result -> {
            results.add(result);
            return QueryResultListener.Progress.CONTINUE;
          }
      );
    }

    QueryParams queryParams = context.getParams();
    int requestSize = queryParams.tryGetResultFilter().getSize();
    int requestFrom = queryParams.tryGetResultFilter().getFrom();
    boolean searchAfterMode = this.useSearchAfterMode(queryParams);

    QueryNodeObject query = buildQueryObject(queryParams, searchAfterMode);
    elasticContext.init(getElasticIndexConfig(), query); // TODO add index config and query node object
    context.getListeners()
        .stream()
        .filter(listener -> listener instanceof CriteriaDecorator)
        .map(CriteriaDecorator.class::cast)
        .forEach(CriteriaDecorator::decorate);

    long totalCount = 0;
    SearchResultMetadata metadata;
    while (true) {
      try {
        metadata = this.executeSearch(query, cls, queryListeners, aggListeners, searchAfterMode);
        long count = metadata.getHits();
        totalCount += count;
        if (searchAfterMode) {  // search after mode is active, so we must exit loop or continue until we get all hits
          if (metadata.getLastSortValues().isEmpty()) {  // the last hit was not set we must exit
            break;
          }
          if (totalCount >= requestSize) {
            break;
          }
          List<?> searchAfterValues = metadata.getLastSortValues();
          query.put("search_after", searchAfterValues);
        } else { // classical i.e. normal search mode
          if (count <= requestSize || requestSize == 0) {  // no more hits will be returned
            break;
          }
        }
      } catch (Exception e) {
        throw new ServiceException(
          ErrorCode.internal_error, "Unexpected error occurred during search execution.",
          Set.of(new AttributeString("errorMessage", e.getMessage())), e
        );
      }
    }

    // set total count to context
    for (RepositoryExecContextListener listener : context.getListeners()) {
      if (listener instanceof TotalCountExecContextListener countExecContextListener) {
        countExecContextListener.onTotalCount(metadata.getTotalHits());
      }
    }

    return results;
  }

  @Override
  public <S extends E, X extends ID> List<S> findById(Collection<X> ids) {
    Collection<String> strIds = ids.stream().map(Object::toString).toList();
    Class<E> cls = getRepositoryExecContext().getRootClass();
    QueryParams params = new QueryParams();
    QueryNodeObject queryObject;
    if (!ids.isEmpty()) {
      if (HasCode.class.isAssignableFrom(cls)) {
        params.condition(and(field("code", in(strIds))));
      } else if (HasUuid.class.isAssignableFrom(cls)) {
        params.condition(and(field("uuid", in(strIds))));
      }
    }
    boolean searchAfterMode = false;
    queryObject = buildQueryObject(params, searchAfterMode);
    List<S> results = new ArrayList<>();
    List<QueryResultListener<E>> resultListeners = List.of(result -> {
      //noinspection unchecked
      results.add((S)result);
      return QueryResultListener.Progress.CONTINUE;
    });

    SearchResultMetadata metadata = this.executeSearch(
        queryObject,
        cls,
        resultListeners,
        new ArrayList<>(),
        searchAfterMode
    );
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
  public <S extends E> List<S> find() {
    throw new ServiceException(
        ErrorCode.internal_error,
        "Unimplemented"
    );
  }
}
