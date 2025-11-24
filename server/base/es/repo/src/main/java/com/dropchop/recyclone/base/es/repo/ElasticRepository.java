package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.api.mapper.TotalCountExecContextListener;
import com.dropchop.recyclone.base.api.model.attr.AttributeDecimal;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.*;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.ConditionOperator;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.base.EsEntity;
import com.dropchop.recyclone.base.es.model.query.IQueryNodeObject;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import com.dropchop.recyclone.base.es.repo.QueryResponseParser.SearchResultMetadata;
import com.dropchop.recyclone.base.es.repo.config.*;
import com.dropchop.recyclone.base.es.repo.listener.AggregationResultConsumer;
import com.dropchop.recyclone.base.es.repo.listener.QueryResultConsumer;
import com.dropchop.recyclone.base.es.repo.marker.AlwaysPresentSearchFields;
import com.dropchop.recyclone.base.es.repo.marker.BlockAllDelete;
import com.dropchop.recyclone.base.es.repo.marker.ConditionStringProvider;
import com.dropchop.recyclone.base.es.repo.query.ElasticQueryBuilder;
import com.dropchop.recyclone.base.es.repo.query.QueryFieldListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
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
public abstract class ElasticRepository<E extends EsEntity, ID> implements
  ElasticCrudRepository<E, ID>, ConditionStringProvider {

  @Getter
  @Setter
  public static class ValidationData {
    private final Set<String> rootFields = new HashSet<>();
    private Condition rootCondition;

    public void addRootField(String field) {
      rootFields.add(field);
    }

    protected void setRootCondition(Condition rootCondition) {
      if (rootCondition == null) {
        return;
      }
      this.rootCondition = rootCondition;
    }
  }

  public static final String HTTP_POST = "POST";
  public static final String ENDPOINT_SEARCH = "/_search";
  public static final String ENDPOINT_DELETE_BY_QUERY = "/_delete_by_query";

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
  public String encodeCondition(IQueryNodeObject queryNodeObject) {
    String result;
    try {
      result = getObjectMapper().writeValueAsString(
          queryNodeObject
      );
    } catch (JsonProcessingException e) {
      throw new ServiceException(
          ErrorCode.data_validation_error,
          "Error in processing conditions: " + this.getClass().getName(),
          e
      );
    }
    return result;
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
    ElasticExecContext<S> context = createRepositoryExecContext();
    context.of(mappingContext);
    Collection<ElasticCriteriaDecorator<S>> decorators = getCommonCriteriaDecorators();
    for (ElasticCriteriaDecorator<S> decorator : decorators) {
      context.decorateWith(decorator);
    }
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
          ErrorCode.process_error,
          "Failed to save entity to Elasticsearch",
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
        ErrorCode.parameter_validation_error,
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

  protected Request buildRequestForSearch(IQueryNodeObject query, String endpoint) {
    ElasticIndexConfig indexConfig = this.getElasticIndexConfig();
    String indexName;
    String postfix = "";
    if (indexConfig instanceof HasTrackTotalHits) {
      postfix = "?track_total_hits=true";
    }
    if (indexConfig instanceof HasQueryBasedReadIndex hasQueryBasedReadIndex) {
      indexName = hasQueryBasedReadIndex.getReadIndexName(query);
      return new Request(HTTP_POST, "/" + indexName + endpoint + postfix);
    }
    if (indexConfig instanceof HasRootAlias hasRootAlias) {
      indexName = hasRootAlias.getRootAlias();
      return new Request(HTTP_POST, "/" + indexName + endpoint + postfix);
    }
    if (indexConfig instanceof HasStaticReadIndex hasStaticReadIndex) {
      indexName = hasStaticReadIndex.getReadIndex();
      return new Request(HTTP_POST, "/" + indexName + endpoint + postfix);
    }
    throw new ServiceException(ErrorCode.internal_error, "No valid index config found!");
  }

  protected void validateRequiredFields(ValidationData validationData, Collection<String> requiredFields) {
    boolean hasRequiredField = validationData.getRootFields().stream()
        .anyMatch(requiredFields::contains);

    if (!hasRequiredField) {
      throw new ServiceException(
          ErrorCode.internal_error,
          String.format(
              "Query validation failed! Operation requires at least one of [%s] fields at the root level!",
              requiredFields
          )
      );
    }
  }

  protected <S extends E> QueryNodeObject buildQuery(ElasticIndexConfig config, ElasticQueryBuilder builder,
                                                     QueryParams params,
                                                     List<ElasticCriteriaDecorator<S>> criteriaDecorators) {
    ValidationData validationData = new ValidationData();
    QueryNodeObject query = builder.build(
        new QueryFieldListener() {
          @Override
          public void on(int level, String fieldName, Condition condition, QueryNodeObject node) {
            if (level == 0) {
              validationData.setRootCondition(condition);
            }
            for (ElasticCriteriaDecorator<S> decorator : criteriaDecorators) {
              decorator.onBuiltField(fieldName, condition, node);
            }
          }

          @Override
          public void on(int level, String fieldName, ConditionOperator operator, QueryNodeObject node) {
            if (level == 0) {
              validationData.addRootField(fieldName);
            }
            for (ElasticCriteriaDecorator<S> decorator : criteriaDecorators) {
              decorator.onBuiltField(fieldName, operator, node);
            }
          }
        },
        config,
        params
    );
    if (this instanceof AlwaysPresentSearchFields alwaysPresentSearchFields) {
      validateRequiredFields(validationData, alwaysPresentSearchFields.anyOf());
    }
    return query;
  }

  @Override
  public IQueryNodeObject provideCondition(QueryParams queryParams, List<CriteriaDecorator<?, ?>> decorators) {
    List<ElasticCriteriaDecorator<E>> criteriaDecorators = new ArrayList<>();
    for (CriteriaDecorator<?, ?> decorator : decorators) {
      if (decorator instanceof ElasticCriteriaDecorator<?> dec) {
        @SuppressWarnings("unchecked")
        ElasticCriteriaDecorator<E> elasticCriteriaDecorator = (ElasticCriteriaDecorator<E>) dec;
        criteriaDecorators.add(elasticCriteriaDecorator);
      }
    }
    return this.buildQuery(getElasticIndexConfig(), getElasticQueryBuilder(), queryParams, criteriaDecorators);
  }

  @Override
  public IQueryNodeObject provideCondition(QueryParams queryParams) {
    return this.provideCondition(queryParams, Collections.emptyList());
  }

  private IQueryNodeObject provideConditionWithMaxSizeWithoutAggregation(ElasticIndexConfig config,
                                                                         QueryParams queryParams,
                                                                         List<CriteriaDecorator<?, ?>> decorators) {
    List<ElasticCriteriaDecorator<E>> criteriaDecorators = new ArrayList<>();
    for (CriteriaDecorator<?, ?> decorator : decorators) {
      if (decorator instanceof ElasticCriteriaDecorator<?> dec) {
        @SuppressWarnings("unchecked")
        ElasticCriteriaDecorator<E> elasticCriteriaDecorator = (ElasticCriteriaDecorator<E>) dec;
        criteriaDecorators.add(elasticCriteriaDecorator);
      }
    }
    IQueryNodeObject result;
    ResultFilter<?, ?> rf = queryParams.tryGetResultFilter();
    String conditionString;
    if (rf != null) {
      int tempFrom = queryParams.tryGetResultFilter().getFrom();
      int tempSize = queryParams.tryGetResultFilter().getSize();
      AggregationList tempAggs = queryParams.getAggregate();
      queryParams.setAggregate(null);
      queryParams.tryGetResultFilter().setFrom(0);
      queryParams.tryGetResultFilter().setSize(getElasticIndexConfig().getSizeOfPagination());
      try {
        result = this.buildQuery(config, getElasticQueryBuilder(), queryParams, criteriaDecorators);
      } finally {
        queryParams.tryGetResultFilter().setFrom(tempFrom);
        queryParams.tryGetResultFilter().setSize(tempSize);
        queryParams.setAggregate(tempAggs);
      }
    } else {
      result = this.buildQuery(config, getElasticQueryBuilder(), queryParams, criteriaDecorators);
    }
    return result;
  }

  @Override
  public IQueryNodeObject provideConditionWithMaxSizeWithoutAggregation(QueryParams queryParams,
                                                                        List<CriteriaDecorator<?, ?>> decorators) {
    return provideConditionWithMaxSizeWithoutAggregation(getElasticIndexConfig(), queryParams, decorators);
  }

  @Override
  public IQueryNodeObject provideConditionWithMaxSizeWithoutAggregation(QueryParams queryParams) {
    return provideConditionWithMaxSizeWithoutAggregation(
        getElasticIndexConfig(), queryParams, Collections.emptyList()
    );
  }

  @Override
  public <S extends E> int deleteByQuery(RepositoryExecContext<S> context) {
    if (this instanceof BlockAllDelete) {
      this.throwDeleteException();
    }
    if (!(context instanceof ElasticExecContext<S> elasticContext)) {
      throw new ServiceException(
          ErrorCode.parameter_validation_error,
          "Invalid context: Expected ElasticExecContext but received " + context.getClass()
      );
    }

    List<ElasticCriteriaDecorator<S>> criteriaDecorators = new ArrayList<>();
    for (RepositoryExecContextListener listener : context.getListeners()) {
      if (listener instanceof ElasticCriteriaDecorator<?> decorator) {
        @SuppressWarnings("unchecked")
        ElasticCriteriaDecorator<S> criteriaDecorator = (ElasticCriteriaDecorator<S>) listener;
        criteriaDecorator.init(elasticContext);
        criteriaDecorators.add(criteriaDecorator);
      }
    }

    QueryParams params = context.getParams();
    QueryNodeObject queryObject = buildQuery(null, getElasticQueryBuilder(), params, criteriaDecorators);


    //query does not allow this in delete
    queryObject.remove("from");

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

  protected <X> SearchResultMetadata executeSearch(QueryParams params,
                                                   QueryNodeObject queryObject,
                                                   Class<X> resultClass,
                                                   List<QueryResultConsumer<X>> queryListeners,
                                                   List<AggregationResultConsumer> aggListeners,
                                                   boolean searchAfterMode) {
    ProfileTimer timer = new ProfileTimer();
    ObjectMapper objectMapper = getObjectMapper();
    String query;
    try {
      query = objectMapper.writeValueAsString(queryObject);
    } catch (IOException e) {
      throw new ServiceException(ErrorCode.internal_error, "Unable to serialize QueryNodeObject", e);
    }

    Request request = null;
    try {
      request = this.buildRequestForSearch(queryObject, ENDPOINT_SEARCH);
      request.setJsonEntity(query);
      Response response = getElasticsearchClient().performRequest(request);
      int queryId = query.hashCode();
      log.debug(
          "Received response for query [{}] on[{}] with[{}] in [{}]ms.",
          queryId, request.getEndpoint(), query, timer.stop()
      );
      if (response.getStatusLine().getStatusCode() == 200) {
        QueryResponseParser parser = new QueryResponseParser(objectMapper, searchAfterMode);
        SearchResultMetadata searchResultMetadata;
        searchResultMetadata = parser.parse(
            response.getEntity().getContent(),
            params,
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
      log.error("Error executing search on endpoint {} with query {}", request.getEndpoint(), query, e);
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
    List<QueryResultConsumer<S>> queryListeners = new ArrayList<>();
    List<AggregationResultConsumer> aggListeners = new ArrayList<>();
    List<ElasticCriteriaDecorator<S>> criteriaDecorators = new ArrayList<>();
    for (RepositoryExecContextListener listener : context.getListeners()) {
      if (listener instanceof QueryResultConsumer<?> queryResultListener) {
        //noinspection unchecked
        queryListeners.add((QueryResultConsumer<S>) queryResultListener);
      }
      if (listener instanceof AggregationResultConsumer aggregationResultConsumer) {
        aggListeners.add(aggregationResultConsumer);
      }
      if (listener instanceof ElasticCriteriaDecorator<?> decorator) {
        @SuppressWarnings("unchecked")
        ElasticCriteriaDecorator<S> criteriaDecorator = (ElasticCriteriaDecorator<S>) listener;
        criteriaDecorator.init(elasticContext);
        criteriaDecorators.add(criteriaDecorator);
      }
    }

    List<S> results = new ArrayList<>();
    if (queryListeners.isEmpty()) {
      // we add a default listener if no listeners were provided so we can return results
      queryListeners.add(
          result -> {
            results.add(result);
            return QueryResultConsumer.Progress.CONTINUE;
          }
      );
    }

    QueryParams queryParams = context.getParams();
    ElasticIndexConfig indexConfig = this.getElasticIndexConfig();
    ElasticQueryBuilder queryBuilder = this.getElasticQueryBuilder();

    int requestSize = queryParams.tryGetResultFilter().getSize();
    int requestFrom = queryParams.tryGetResultFilter().getFrom();
    boolean searchAfterMode = queryBuilder.useSearchAfter(indexConfig, queryParams);

    if (requestSize >= indexConfig.getSizeOfPagination() && searchAfterMode) {
      queryParams.tryGetResultFilter().setSize(indexConfig.getSizeOfPagination());
    }

    QueryNodeObject query = buildQuery(
        indexConfig,
        queryBuilder,
        queryParams,
        criteriaDecorators
    );

    elasticContext.init(getElasticIndexConfig(), query);
    criteriaDecorators.forEach(CriteriaDecorator::decorate);

    long totalCount = 0;
    SearchResultMetadata metadata;
    while (true) {
      metadata = this.executeSearch(queryParams, query, cls, queryListeners, aggListeners, searchAfterMode);
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
    ElasticExecContext<S> context = getRepositoryExecContext();
    Class<S> cls = context.getRootClass();
    QueryParams queryParams = new QueryParams();
    queryParams.tryGetResultFilter().setSize(ids.size());
    QueryNodeObject queryObject;
    if (!ids.isEmpty()) {
      if (HasCode.class.isAssignableFrom(cls)) {
        queryParams.condition(and(field("code", in(strIds))));
      } else if (HasUuid.class.isAssignableFrom(cls)) {
        queryParams.condition(and(field("uuid", in(strIds))));
      } else if (HasId.class.isAssignableFrom(cls)) {
        queryParams.condition(and(field("id", in(strIds))));
      } else {
        throw new ServiceException(
            ErrorCode.parameter_validation_error,
            "Unable to find entities by id. Entity class " + cls.getSimpleName() + " does not implement any of ["
                + Set.of(HasId.class, HasUuid.class, HasCode.class) + "]!"
        );
      }
    }
    context.setParams(queryParams);
    return this.search(context);
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
