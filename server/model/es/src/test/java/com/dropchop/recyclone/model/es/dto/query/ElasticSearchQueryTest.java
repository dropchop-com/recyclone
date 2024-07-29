package com.dropchop.recyclone.model.es.dto.query;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ElasticSearchQueryTest {

  @Test
  public void simpleSearchQuery() {
    String actualJson = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"status\":\"active\"}}],\"should\":[],\"must_not\":[]}}}";

    QueryFilter<String> filter = new QueryFilter("status", QueryFilter.Operation.EQUAL, "active", QueryFilter.LogicalOperator.AND);
    ElasticSearchQuery es = new ElasticSearchQuery(List.of(filter), List.of());
    assertEquals(actualJson, es.buildJson());
  }

  @Test
  public void testRangeQueryGreaterThan() {
    String expectedJson = "{\"query\":{\"bool\":{\"must\":[{\"range\":{\"age\":{\"gt\":18}}}],\"should\":[],\"must_not\":[]}}}";
    QueryFilter<Number> filter = new QueryFilter("age", QueryFilter.Operation.GREATER_THAN, 18, QueryFilter.LogicalOperator.AND);
    ElasticSearchQuery query = new ElasticSearchQuery(List.of(filter), List.of());
    assertEquals(expectedJson, query.buildJson());
  }

  @Test
  public void testRangeQueryLessThan() {
    String expectedJson = "{\"query\":{\"bool\":{\"must\":[{\"range\":{\"price\":{\"lt\":500}}}],\"should\":[],\"must_not\":[]}}}";
    QueryFilter<Number> filter = new QueryFilter("price", QueryFilter.Operation.LESS_THAN, 500, QueryFilter.LogicalOperator.AND);
    ElasticSearchQuery query = new ElasticSearchQuery(List.of(filter), List.of());
    assertEquals(expectedJson, query.buildJson());
  }

  @Test
  public void testMultipleMustFilters() {
    String expectedJson = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"status\":\"active\"}},{\"term\":{\"verified\":true}}],\"should\":[],\"must_not\":[]}}}";
    QueryFilter<String> filter1 = new QueryFilter("status", QueryFilter.Operation.EQUAL, "active", QueryFilter.LogicalOperator.AND);
    QueryFilter<Boolean> filter2 = new QueryFilter("verified", QueryFilter.Operation.EQUAL, true, QueryFilter.LogicalOperator.AND);
    ElasticSearchQuery query = new ElasticSearchQuery(List.of(filter1, filter2), List.of());
    assertEquals(expectedJson, query.buildJson());
  }

  @Test
  public void testMustAndShouldFilters() {
    String expectedJson = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"status\":\"active\"}}],\"should\":[{\"term\":{\"priority\":\"high\"}}],\"must_not\":[]}}}";
    QueryFilter<String> mustFilter = new QueryFilter("status", QueryFilter.Operation.EQUAL, "active", QueryFilter.LogicalOperator.AND);
    QueryFilter<String> shouldFilter = new QueryFilter("priority", QueryFilter.Operation.EQUAL, "high", QueryFilter.LogicalOperator.OR);
    ElasticSearchQuery query = new ElasticSearchQuery(List.of(mustFilter, shouldFilter), List.of());
    assertEquals(expectedJson, query.buildJson());
  }

  @Test
  public void testExistsQuery() {
    String expectedJson = "{\"query\":{\"bool\":{\"must\":[{\"exists\":{\"field\":\"email\"}}],\"should\":[],\"must_not\":[]}}}";
    QueryFilter<String> filter = new QueryFilter("email", QueryFilter.Operation.EXISTS, null, QueryFilter.LogicalOperator.AND);
    ElasticSearchQuery query = new ElasticSearchQuery(List.of(filter), List.of());
    assertEquals(expectedJson, query.buildJson());
  }

  @Test
  public void testNotFilter() {
    String expectedJson = "{\"query\":{\"bool\":{\"must\":[],\"should\":[],\"must_not\":[{\"term\":{\"status\":\"inactive\"}}]}}}";
    QueryFilter<String> notFilter = new QueryFilter("status", QueryFilter.Operation.EQUAL, "inactive", QueryFilter.LogicalOperator.NOT);
    ElasticSearchQuery query = new ElasticSearchQuery(List.of(notFilter), List.of());
    assertEquals(expectedJson, query.buildJson());
  }

  @Test
  public void testComplexQueryWithAggregation() {
    String expectedJson = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"status\":\"active\"}}],\"should\":[],\"must_not\":[]}},\"aggs\":{\"avg_price\":{\"avg\":{\"field\":\"price\"}}}}";
    QueryFilter<String> filter = new QueryFilter("status", QueryFilter.Operation.EQUAL, "active", QueryFilter.LogicalOperator.AND);
    QueryAggregation aggregation = new QueryAggregation("price", QueryAggregation.AggregateType.AVG);
    ElasticSearchQuery query = new ElasticSearchQuery(List.of(filter), List.of(aggregation));
    assertEquals(expectedJson, query.buildJson());
  }

  @Test
  public void testTermsQueryWithMultipleValues() {
    String expectedJson = "{\"query\":{\"bool\":{\"must\":[{\"terms\":{\"tags\":[\"blue\",\"red\"]}}],\"should\":[],\"must_not\":[]}}}";
    List<String> values = List.of("blue", "red");
    QueryFilter<String> filter = new QueryFilter("tags", QueryFilter.Operation.IN, values, QueryFilter.LogicalOperator.AND);
    ElasticSearchQuery query = new ElasticSearchQuery(List.of(filter), List.of());
    assertEquals(expectedJson, query.buildJson());
  }

  @Test
  public void testMultipleAggregations() {
    String expectedJson = "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"status\":\"active\"}}],\"should\":[],\"must_not\":[]}},\"aggs\":{\"count_tags\":{\"terms\":{\"field\":\"tags\"}},\"max_price\":{\"max\":{\"field\":\"price\"}}}}";
    QueryFilter<String> filter = new QueryFilter("status", QueryFilter.Operation.EQUAL, "active", QueryFilter.LogicalOperator.AND);
    QueryAggregation agg1 = new QueryAggregation("tags", QueryAggregation.AggregateType.COUNT);
    QueryAggregation agg2 = new QueryAggregation("price", QueryAggregation.AggregateType.MAX);
    ElasticSearchQuery query = new ElasticSearchQuery(List.of(filter), List.of(agg1, agg2));
    assertEquals(expectedJson, query.buildJson());
  }

  @Test
  public void complexQueryWithMultipleFiltersAndAggregations() {
    String expectedJson = "{\"query\":{\"bool\":{\"must\":[{\"range\":{\"price\":{\"gte\":100,\"lte\":500}}}],\"should\":[{\"terms\":{\"tags\":[\"new\",\"featured\"]}}],\"must_not\":[{\"term\":{\"stock\":\"out of stock\"}}]}},\"aggs\":{\"avg_price\":{\"avg\":{\"field\":\"price\"}},\"count_category\":{\"terms\":{\"field\":\"category\"}},\"max_rating\":{\"max\":{\"field\":\"rating\"}}}}";

    QueryFilter<Number> priceFilter = new QueryFilter("price", QueryFilter.Operation.RANGE, List.of(100, 500), QueryFilter.LogicalOperator.AND);
    QueryFilter<List<String>> tagsFilter = new QueryFilter("tags", QueryFilter.Operation.IN, List.of("new", "featured"), QueryFilter.LogicalOperator.OR);
    QueryFilter<String> stockFilter = new QueryFilter("stock", QueryFilter.Operation.EQUAL, "out of stock", QueryFilter.LogicalOperator.NOT);

    QueryAggregation avgPriceAgg = new QueryAggregation("price", QueryAggregation.AggregateType.AVG);
    QueryAggregation categoryCountAgg = new QueryAggregation("category", QueryAggregation.AggregateType.COUNT);
    QueryAggregation maxRatingAgg = new QueryAggregation("rating", QueryAggregation.AggregateType.MAX);

    ElasticSearchQuery query = new ElasticSearchQuery(List.of(priceFilter, tagsFilter, stockFilter), List.of(avgPriceAgg, categoryCountAgg, maxRatingAgg));

    assertEquals(expectedJson, query.buildJson());
  }
}
