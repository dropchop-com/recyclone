package com.dropchop.recyclone.model.api.query;

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
      "cardinality", Cardinality.class,
      "dateHistogram", DateHistogram.class
    );
  }

  // Static factory methods for building aggregations
  static Max max(String name, String field, Aggregation... subAggregations) {
    return new Max(name, field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Max max(String name, String field, List<Aggregation> subAggregations) {
    return new Max(name, field, subAggregations);
  }

  static Min min(String name, String field, Aggregation... subAggregations) {
    return new Min(name, field, subAggregations);
  }

  static Sum sum(String name, String field, Aggregation... subAggregations) {
    return new Sum(name, field, subAggregations);
  }

  static Count count(String name, String field, Aggregation... subAggregations) {
    return new Count(name, field, subAggregations);
  }

  static Avg avg(String name, String field, Aggregation... subAggregations) {
    return new Avg(name, field, subAggregations);
  }

  static Cardinality cardinality(String name, String field, Aggregation... subAggregations) {
    return new Cardinality(name, field, subAggregations);
  }

  static DateHistogram dateHistogram(String name, String field, Aggregation... subAggregations) {
    return new DateHistogram(name, field, subAggregations);
  }
}
