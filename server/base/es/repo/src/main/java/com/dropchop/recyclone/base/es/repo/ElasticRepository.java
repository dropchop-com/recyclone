package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.repo.ElasticCrudRepository;
import com.dropchop.recyclone.base.api.repo.config.DateBasedIndexConfig;
import com.dropchop.recyclone.base.api.repo.config.DefaultIndexConfig;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.dropchop.recyclone.base.api.model.query.Condition.and;
import static com.dropchop.recyclone.base.api.model.query.Condition.field;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.in;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class ElasticRepository<E extends EsEntity, ID> implements ElasticCrudRepository<E, ID>,
  ElasticBulkMethod {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  ExecContextContainer ctxContainer;

  public abstract ElasticQueryMapper getElasticQueryMapper();

  public abstract ObjectMapper getObjectMapper();

  public abstract RestClient getElasticsearchClient();

  public String getClassAlias(Class<?> cls) {
    return cls.getSimpleName().toLowerCase();
  }

  public abstract com.dropchop.recyclone.base.api.repo.config.ElasticIndexConfig getElasticIndexConfig();

  protected Collection<CriteriaDecorator> getCommonCriteriaDecorators() {
    return List.of(
      new PageCriteriaDecorator()
    );
  }

  @Override
  public RepositoryExecContext<E> getRepositoryExecContext() {
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

  protected <S extends E> List<S> executeBulkRequest(Collection<S> entities, ElasticBulkMethodImpl method) {
    if (entities == null || entities.isEmpty()) {
      return Collections.emptyList();
    }

    StringBuilder bulkRequestBody = new StringBuilder();
    ObjectMapper objectMapper = getObjectMapper();

    bulkRequestBody = method.buildBulkRequest(entities, bulkRequestBody, objectMapper);

    StringBuilder endpoint = new StringBuilder();
    endpoint.append("/_bulk");

    if(getElasticIndexConfig().getIngestPipeline() != null) {
      endpoint.append("?pipeline=").append(getElasticIndexConfig().getIngestPipeline());
    }

    Request request = new Request("POST", endpoint.toString());
    request.setJsonEntity(bulkRequestBody.toString());

    try {
      Response response = getElasticsearchClient().performRequest(request);
      return method.checkSuccessfulResponse(entities, response, objectMapper);
    } catch (ServiceException | IOException e) {
      throw new ServiceException(
        ErrorCode.unknown_error, "Failed to save entity to Elasticsearch",
        Set.of(new AttributeString("error", e.getMessage())), e
      );
    }
  }

  @Override
  public <S extends E> List<S> save(Collection<S> entities) {
    ElasticBulkMethodImpl saveBulkMethod = new ElasticBulkMethodImpl(
      ElasticBulkMethodImpl.MethodType.INDEX) {

      @Override
      protected <P extends EsEntity> String getIndexOuterName(P entity) {
        if(getElasticIndexConfig() instanceof DateBasedIndexConfig) {
          if(entity instanceof HasCreated && ((HasCreated) entity).getCreated() != null) {
            return ((DateBasedIndexConfig) getElasticIndexConfig())
              .getMonthBasedIndexName(((HasCreated) entity).getCreated(), entity.getClass()).getFirst();
          } else {
            throw new ServiceException(
              ErrorCode.internal_error,
              "No Created field found for entity " + entity.getClass() +
                " but the requested implementation is using DateBasedIndexConfig"
            );
          }
        } else {
          return getElasticIndexConfig().getIndexName(entity);
        }
      }
    };
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
  public <X extends ID> int deleteById(Collection<X> ids) {
    if (ids == null || ids.isEmpty()) {
      return 0;
    }

    Class<E> rootClass = getRootClass();

    StringBuilder bulkRequestBody = new StringBuilder();
    ObjectMapper objectMapper = getObjectMapper();

    for (X id : ids) {
      bulkRequestBody
        .append("{ \"delete\" : { \"_index\" : \"")
        .append(getElasticIndexConfig().getRootClassName(this.getRootClass()))
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
  }

  @Override
  public <X extends ID> int deleteById(X id) {
    return deleteById(Collections.singletonList(id));
  }

  @Override
  public <S extends E> List<S> delete(Collection<S> entities) {
    ElasticBulkMethodImpl elasticDeleteMethod = new ElasticBulkMethodImpl(
      ElasticBulkMethodImpl.MethodType.DELETE) {
      @Override
      protected <P extends EsEntity> String getIndexOuterName(P entity) {
        if(getElasticIndexConfig() instanceof DateBasedIndexConfig) {
          if(entity instanceof HasCreated && ((HasCreated) entity).getCreated() != null) {
            return ((DateBasedIndexConfig) getElasticIndexConfig())
              .getMonthBasedIndexName(((HasCreated) entity).getCreated(), entity.getClass()).getFirst();
          } else {
            throw new ServiceException(
              ErrorCode.internal_error,
              "No Created field found for entity " + entity.getClass() +
                " but the requested implementation is using DateBasedIndexConfig"
            );
          }
        } else {
          return getElasticIndexConfig().getIndexName(entity);
        }
      }
    };

    return executeBulkRequest(entities, elasticDeleteMethod);
  }

  @Override
  public <S extends E> S delete(S entity) {
    return delete(List.of(entity)).getFirst();
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
        ErrorCode.internal_error,
        "Unable to serialize QueryNodeObject",
        e
      );
    }

    try {
      Request request = new Request("POST",
        "/" + getElasticIndexConfig().getRootClassName(this.getRootClass()) + "/_delete_by_query");
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
            new AttributeString("status", String.valueOf(response.getStatusLine().getStatusCode())),
            new AttributeString("query", query)
          )
        );
      }
    } catch (IOException e) {
      throw new ServiceException(
        ErrorCode.internal_error, "Unable to execute delete query",
        Set.of(new AttributeString("delete query", query)), e
      );
    }
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
      if (HasCreated.class.isAssignableFrom(rootClass)) {
        defaultSort.put("created", "desc");
        sortOrder.put("sort", defaultSort);
      } else if (HasUuid.class.isAssignableFrom(rootClass)) {
        defaultSort.put("uuid", "desc");
        sortOrder.put("sort", defaultSort);
      } else if (HasCode.class.isAssignableFrom(rootClass)) {
        defaultSort.put("code", "desc");
        sortOrder.put("sort", defaultSort);
      } else if (HasId.class.isAssignableFrom(rootClass)) {
        defaultSort.put("is", "desc");
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

  private QueryNodeObject buildQueryObject(QueryParams params,
                                           QueryNodeObject sortOrder,
                                           List<Object> searchAfterValues) {

    int sizeOfPagination = getElasticIndexConfig().getSizeOfPagination();
    int maxSize = params.tryGetResultFilter().size();
    int from = params.tryGetResultFilter().from();

    ElasticQueryMapper esQueryMapper = new ElasticQueryMapper();
    QueryNodeObject initialQueryObject = esQueryMapper.mapToString(params);

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

  @Override
  public <S extends E, X extends ID> S findById(X id) {
    List<S> entities = findById(List.of(id));
    return entities.isEmpty() ? null : entities.getFirst();
  }

  private <X> void executeQuery(QueryNodeObject queryObject, boolean skipParsing,
                                Collection<RepositoryExecContextListener> listeners) {
    ProfileTimer timer = new ProfileTimer();
    ObjectMapper objectMapper = getObjectMapper();
    String query;
    try {
      query = objectMapper.writeValueAsString(queryObject);
    } catch (IOException e) {
      throw new ServiceException(
        ErrorCode.internal_error,
        "Unable to serialize QueryNodeObject",
        e
      );
    }
    Class<E> cls = getRootClass();
    try {
      Request request = buildRequest(queryObject);

      request.setJsonEntity(query);
      Response response = getElasticsearchClient().performRequest(request);
      if (response.getStatusLine().getStatusCode() == 200) {
        String responseBody = EntityUtils.toString(response.getEntity());
        ElasticSearchResult<X> searchResult;
        if (skipParsing) {
          searchResult = objectMapper.readValue(
            responseBody, new TypeReference<>() {
            }
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
            if(listener instanceof QueryResultListener queryResultListener) {
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

  private Request buildRequest(QueryNodeObject query) {
    if (getElasticIndexConfig() instanceof DateBasedIndexConfig) {
      return buildDateBasedRequest((DateBasedIndexConfig) getElasticIndexConfig());
    } else if (getElasticIndexConfig() instanceof DefaultIndexConfig) {
      return buildDefaultRequest((DefaultIndexConfig) getElasticIndexConfig());
    } else {
      throw new ServiceException(
        ErrorCode.internal_error,
        "No valid ElasticIndexConfig found for class ElasticRepository!"
      );
    }
  }

  private Request buildDateBasedRequest(DateBasedIndexConfig dateConfig) {
    if (dateConfig.getAlias() != null) {
      String alias = dateConfig.getAlias();
      log.debug("Using alias [{}] as set in ElasticIndexConfig", alias);
      return new Request("GET", "/" + alias + "/_search");
    } else {
      List<String> indexNames = dateConfig.getMonthBasedIndexName(ZonedDateTime.now(), getRootClass());
      String indexName = String.join(",", indexNames);
      log.debug("Alias was not set in ElasticIndexConfig, using current time to calculate index [{}]", indexName);
      return new Request("GET", "/" + indexName + "/_search");
    }
  }

  private Request buildDefaultRequest(DefaultIndexConfig defaultIndexConfig) {
    if (defaultIndexConfig.getAlias() != null) {
      String alias = defaultIndexConfig.getAlias();
      log.debug("Using alias [{}] as set in ElasticIndexConfig", alias);
      return new Request("GET", "/" + alias + "/_search");
    } else {
      String indexName = getElasticIndexConfig().getRootClassName(this.getRootClass());
      log.debug("Using default index configuration with index name [{}]", indexName);
      return new Request("GET", "/" + indexName + "/_search");
    }
  }

  //TODO: Think about correct implementation of this...
  @Override
  public List<String> setDatesForIndices(QueryNodeObject query) {
    UUID uuid = query.getNestedValue(UUID.class, "uuid");
    ZonedDateTime created = query.getNestedValue(ZonedDateTime.class, "created");

    if (created != null) {
      return ((DateBasedIndexConfig) getElasticIndexConfig())
        .getMonthBasedIndexName(created, getRootClass());
    } else if (uuid != null) {
      try {
        if (uuid.version() != 1) {
          throw new ServiceException(
            ErrorCode.internal_error,
            "UUID must be version 1 (time-based) for timestamp extraction"
          );
        }

        long uuidTimestamp = uuid.timestamp();

        final long EPOCH_DIFF = 12219292800L * 1_000_000_000L;
        long unixMillis = ((uuidTimestamp * 100) - EPOCH_DIFF) / 1_000_000L;

        ZonedDateTime creationDate = Instant.ofEpochMilli(unixMillis)
          .atZone(ZoneId.systemDefault());

        return ((DateBasedIndexConfig) getElasticIndexConfig())
          .getMonthBasedIndexName(creationDate, getRootClass());

      } catch (ServiceException e) {
        log.warn("UUID must be version 1 (time-based) for timestamp extraction");
        return ((DateBasedIndexConfig) getElasticIndexConfig())
          .getMonthBasedIndexName(ZonedDateTime.now(), getRootClass());
      } catch (Exception e) {
        throw new ServiceException(
          ErrorCode.internal_error,
          "Failed to extract timestamp from UUID: " + e.getMessage(),
          e
        );
      }
    } else {
      log.warn("No date specification found in query, using current time in ElasticRepository");
      return ((DateBasedIndexConfig) getElasticIndexConfig())
        .getMonthBasedIndexName(ZonedDateTime.now(), getRootClass());
    }
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
    queryObject = buildQueryObject(params, null, null);
    List<S> results = new ArrayList<>();
    executeQuery(queryObject, false, List.of(
      (QueryResultListener<S>) result -> results.add(result.getSource())
    ));
    return results;
  }

  @Override
  public <S extends E> List<S> find(RepositoryExecContext<S> context) {
    if (!(context instanceof ElasticExecContext<S> elasticContext)) {
      throw new ServiceException(
        ErrorCode.parameter_validation_error,
        "Invalid context: Expected ElasticExecContext but received " + context.getClass()
      );
    }
    //TODO: any params can be used here with the correct criteria decorators
    //      this impl is false
    return this.search((QueryParams) elasticContext.getParams(), elasticContext);
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


  private void applyDecorators(RepositoryExecContext<?> context) {
    context.getListeners().stream()
      .filter(listener -> listener instanceof CriteriaDecorator)
      .map(listener -> (CriteriaDecorator) listener)
      .forEach(CriteriaDecorator::decorate);
  }

  private <S> List<Object> getSearchAfterValues(List<Hit<S>> hits) {
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

    applyDecorators(context);

    List<Object> searchAfterValues = null;
    QueryNodeObject sortOrder = buildSortOrder(
      context.getParams().tryGetResultFilter().sort(), elasticContext.getRootClass()
    );

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

    int maxSize = params.tryGetResultFilter().getSize();

    boolean hasMoreHits = true;
    QueryParams queryParams = context.getParams();
    List<S> results = new ArrayList<>();
    while (hasMoreHits && results.size() < maxSize) {
      try {
        QueryNodeObject queryObject = buildQueryObject(queryParams, sortOrder, searchAfterValues);
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
}
