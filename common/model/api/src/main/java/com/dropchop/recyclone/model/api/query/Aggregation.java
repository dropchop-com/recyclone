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
      "terms", Terms.class,
      "cardinality", Cardinality.class,
      "dateHistogram", DateHistogram.class,
      "aggregationField", AggregationField.class
    );
  }

  // Static factory methods for building aggregations
  static Max max(Aggregation... subAggregations) {
    return new Max(new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Max max(List<Aggregation> subAggregations) {
    return new Max(subAggregations);
  }

  static Min min(Aggregation... subAggregations) {
    return new Min(new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Min min(List<Aggregation> subAggregations) {
    return new Min(subAggregations);
  }

  static Sum sum(Aggregation... subAggregations) {
    return new Sum(new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Sum sum(List<Aggregation> subAggregations) {
    return new Sum(subAggregations);
  }

  static Count count(Aggregation... subAggregations) {
    return new Count(new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Count count(List<Aggregation> subAggregations) {
    return new Count(subAggregations);
  }

  static Avg avg(Aggregation... subAggregations) {
    return new Avg(new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Avg avg(List<Aggregation> subAggregations) {
    return new Avg(subAggregations);
  }

  static Cardinality cardinality(Aggregation... subAggregations) {
    return new Cardinality(new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Cardinality cardinality(List<Aggregation> subAggregations) {
    return new Cardinality(subAggregations);
  }

  static DateHistogram dateHistogram(Aggregation... subAggregations) {
    return new DateHistogram(new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static DateHistogram dateHistogram(List<Aggregation> subAggregations) {
    return new DateHistogram(subAggregations);
  }

  static Terms terms(Aggregation... subAggregations) {
    return new Terms(new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Terms terms(List<Aggregation> subAggregations) {
    return new Terms(subAggregations);
  }

  static AggregationField aggregationField(String name, String field) {
    return new AggregationField(name, field);
  }

  static AggregationField aggregationHistogramField(String name, String field, String calendarInterval) {
    return new AggregationField(name, field, calendarInterval);
  }
}
