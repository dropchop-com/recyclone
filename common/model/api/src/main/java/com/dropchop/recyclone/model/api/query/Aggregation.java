package com.dropchop.recyclone.model.api.query;

import com.dropchop.recyclone.model.api.query.aggregation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface Aggregation {

  static Map<String, Class<? extends Aggregation>> supported() {
    return Map.of(
      "sum", Sum.class,
      "min", Min.class,
      "max", Max.class,
      "count", Count.class,
      "avg", Avg.class,
      "terms", Terms.class,
      "cardinality", Cardinality.class,
      "dateHistogram", DateHistogram.class
    );
  }

  static List<AggregationContainer> aggs(Aggregation ... aggs) {
    return Arrays.stream(aggs).map(AggregationContainer::new).toList();
  }

  static Max2 max(String name, String field, Aggregation... subAggregations) {
    return new Max2(name, field, subAggregations);
  }

  // Static factory methods for building aggregations
  /*static Max max(String name, String field, Aggregation... subAggregations) {
    return new Max(name, field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Max max(String name, String field, List<Aggregation> subAggregations) {
    return new Max(name, field, subAggregations);
  }

  static Min min(String name, String field, Aggregation... subAggregations) {
    return new Min(name, field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Min min(String name, String field, List<Aggregation> subAggregations) {
    return new Min(name, field, subAggregations);
  }

  static Sum sum(String name, String field, Aggregation... subAggregations) {
    return new Sum(name, field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Sum sum(String name, String field, List<Aggregation> subAggregations) {
    return new Sum(name, field, subAggregations);
  }

  static Count count(String name, String field, Aggregation... subAggregations) {
    return new Count(name, field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Count count(String name, String field, List<Aggregation> subAggregations) {
    return new Count(name, field, subAggregations);
  }

  static Avg avg(String name, String field, Aggregation... subAggregations) {
    return new Avg(name, field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Avg avg(String name, String field, List<Aggregation> subAggregations) {
    return new Avg(name, field, subAggregations);
  }

  static Cardinality cardinality(String name, String field, Aggregation... subAggregations) {
    return new Cardinality(name, field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Cardinality cardinality(String name, String field, List<Aggregation> subAggregations) {
    return new Cardinality(name, field, subAggregations);
  }

  static DateHistogram dateHistogram(String name, String field, String calendar_interval, Aggregation... subAggregations) {
    return new DateHistogram(name, field, calendar_interval, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static DateHistogram dateHistogram(String name, String field, String calendar_interval, List<Aggregation> subAggregations) {
    return new DateHistogram(name, field, calendar_interval, subAggregations);
  }

  static Terms terms(String name, String field, Aggregation... subAggregations) {
    return new Terms(name, field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Terms terms(String name, String field, List<Aggregation> subAggregations) {
    return new Terms(name, field, subAggregations);
  }*/
}
