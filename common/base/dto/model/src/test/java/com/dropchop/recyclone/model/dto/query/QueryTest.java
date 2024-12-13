package com.dropchop.recyclone.model.dto.query;

import com.dropchop.recyclone.base.api.model.query.AggregationCriteria;
import com.dropchop.recyclone.base.api.model.query.FilterCriteria;
import com.dropchop.recyclone.base.api.model.query.SortCriterion;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for Query DTO to ensure pagination, sorting, and filtering are correctly implemented.
 */
class QueryTest {

  @Test
  void testPagination() {
    Query query = new Query();
    query.setPage(1);
    query.setSize(10);

    assertEquals(1, query.getPage());
    assertEquals(10, query.getSize());
  }

  @Test
  void testSorting() {
    Query query = new Query();
    SortCriterion sortCriterion1 = new SortCriterion("name", "asc");
    SortCriterion sortCriterion2 = new SortCriterion("date", "desc");
    query.setSortCriteria(List.of(sortCriterion1, sortCriterion2));

    assertEquals(2, query.getSortCriteria().size());
    assertEquals("name", query.getSortCriteria().get(0).getField());
    assertEquals("asc", query.getSortCriteria().get(0).getDirection());
    assertEquals("date", query.getSortCriteria().get(1).getField());
    assertEquals("desc", query.getSortCriteria().get(1).getDirection());
  }

  @Test
  void testFiltering() {
    Query query = new Query();
    FilterCriteria filterCriteria = new FilterCriteria("status", "EQUAL", "active");
    query.setFilterCriteria(filterCriteria);

    assertEquals("status", query.getFilterCriteria().getField());
    assertEquals("EQUAL", query.getFilterCriteria().getOperation());
    assertEquals("active", query.getFilterCriteria().getValue());
  }

  @Test
  void testComplexQuery() {
    Query query = new Query();
    query.setPage(1);
    query.setSize(20);

    SortCriterion sortCriterion = new SortCriterion("relevance", "desc");
    query.setSortCriteria(List.of(sortCriterion));

    FilterCriteria filterCriteria = new FilterCriteria("category", "IN", List.of("books", "electronics"));
    query.setFilterCriteria(filterCriteria);

    assertEquals(1, query.getPage());
    assertEquals(20, query.getSize());
    assertEquals(1, query.getSortCriteria().size());
    assertEquals("relevance", query.getSortCriteria().getFirst().getField());
    assertEquals("desc", query.getSortCriteria().getFirst().getDirection());
    assertEquals("category", query.getFilterCriteria().getField());
    assertEquals("IN", query.getFilterCriteria().getOperation());
    assertTrue(((List<?>) query.getFilterCriteria().getValue()).contains("books"));
    assertTrue(((List<?>) query.getFilterCriteria().getValue()).contains("electronics"));
  }

  @Test
  void testAggregations() {
    Query query = new Query();
    AggregationCriteria agg1 = new AggregationCriteria("total_price", "sum");
    AggregationCriteria agg2 = new AggregationCriteria("average_rating", "avg");
    query.setAggregations(List.of(agg1, agg2));

    assertEquals(2, query.getAggregations().size());
    assertEquals("total_price", query.getAggregations().get(0).getField());
    assertEquals("sum", query.getAggregations().get(0).getType());
    assertEquals("average_rating", query.getAggregations().get(1).getField());
    assertEquals("avg", query.getAggregations().get(1).getType());
  }

  @Test
  void testComplexQueryWithAggregations() {
    Query query = new Query();
    query.setPage(1);
    query.setSize(20);

    SortCriterion sortCriterion = new SortCriterion("relevance", "desc");
    query.setSortCriteria(List.of(sortCriterion));

    FilterCriteria filterCriteria = new FilterCriteria("category", "IN", List.of("books", "electronics"));
    query.setFilterCriteria(filterCriteria);

    AggregationCriteria agg = new AggregationCriteria("count_users", "count");
    query.setAggregations(List.of(agg));

    assertEquals(1, query.getPage());
    assertEquals(20, query.getSize());
    assertEquals(1, query.getSortCriteria().size());
    assertEquals("relevance", query.getSortCriteria().getFirst().getField());
    assertEquals("desc", query.getSortCriteria().getFirst().getDirection());
    assertEquals("category", query.getFilterCriteria().getField());
    assertEquals("IN", query.getFilterCriteria().getOperation());
    assertTrue(((List<?>) query.getFilterCriteria().getValue()).contains("books"));
    assertTrue(((List<?>) query.getFilterCriteria().getValue()).contains("electronics"));
    assertEquals(1, query.getAggregations().size());
    assertEquals("count_users", query.getAggregations().getFirst().getField());
    assertEquals("count", query.getAggregations().getFirst().getType());
  }
}