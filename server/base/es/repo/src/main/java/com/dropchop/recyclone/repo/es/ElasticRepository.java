package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.base.api.model.utils.Strings;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.api.repo.ElasticCrudRepository;
import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import com.dropchop.recyclone.base.api.repo.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.es.listener.MapResultListener;
import com.dropchop.recyclone.repo.es.listener.QueryResultListener;
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.dropchop.recyclone.repo.es.mapper.ElasticSearchResult;
import com.dropchop.recyclone.repo.es.mapper.ElasticSearchResult.Container;
import com.dropchop.recyclone.repo.es.mapper.ElasticSearchResult.Hit;
import com.dropchop.recyclone.repo.es.mapper.QueryNodeObject;
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
public abstract class ElasticRepository<E extends Model, ID> implements ElasticCrudRepository<E, ID> {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  ExecContextContainer ctxContainer;

  protected abstract ElasticQueryMapper getElasticQueryMapper();

  protected abstract ObjectMapper getObjectMapper();

  protected abstract RestClient getElasticsearchClient();

  public String getClassAlias(Class<?> cls) {
    return cls.getSimpleName().toLowerCase();
  }

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

  protected String getIndexName() {
    String simpleName = getRootClass().getSimpleName();
    if (simpleName.startsWith("Es")) {
      simpleName = simpleName.substring(2);
    }
    return Strings.toSnakeCase(simpleName);
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
      throw new ServiceException(
        ErrorCode.unknown_error, "Failed to save entity to Elasticsearch",
        Set.of(new AttributeString("error", e.getMessage()))
      );
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
        .append(getIndexName())
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
      Request request = new Request("POST", "/" + getIndexName() + "/_delete_by_query");
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
    int size = params.tryGetResultFilter().size();
    int from = params.tryGetResultFilter().from();

    ElasticQueryMapper esQueryMapper = new ElasticQueryMapper();
    QueryNodeObject initialQueryObject = esQueryMapper.mapToString(params);
    initialQueryObject.put("size", Math.min(size, 10000));

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
                                Collection<QueryResultListener<X>> listeners) {
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
      Request request = new Request("GET", "/" + getIndexName() + "/_search");
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
          for (QueryResultListener<X> listener : listeners) {
            listener.onResult(hit);
          }
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
      queryObject = buildQueryObject(params, null, null);
    } else {
      queryObject = buildQueryObject(params, null, null);
    }
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
    if (listener == null && elasticContext.isSkipObjectParsing()) {
      throw new ServiceException(
        ErrorCode.internal_error,
        "Skip object parsing was enabled but there is no raw result listener. Such implementation makes no sense!"
      );
    }

    boolean hasMoreHits = true;
    QueryParams queryParams = context.getParams();
    List<S> results = new ArrayList<>();
    while (hasMoreHits) {
      try {
        QueryNodeObject queryObject = buildQueryObject(queryParams, sortOrder, searchAfterValues);
        Container<Hit<S>> last = new Container<>();
        if (((ElasticExecContext<S>) context).isSkipObjectParsing() && listener != null) {
          executeQuery(queryObject, true, List.of(
            (QueryResultListener<S>) last::setHit
          ));
        } else {
          executeQuery(queryObject, false, List.of(
            (QueryResultListener<S>) hit -> {
              results.add(hit.getSource());
              last.setHit(hit);
            }
          ));
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
