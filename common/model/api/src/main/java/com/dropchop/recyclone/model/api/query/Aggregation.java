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
  static Max max(AggregationField field, Aggregation... subAggregations) {
    return new Max(field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Max max(AggregationField field, List<Aggregation> subAggregations) {
    return new Max(field, subAggregations);
  }

  static Min min(AggregationField field, Aggregation... subAggregations) {
    return new Min(field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Min min(AggregationField field, List<Aggregation> subAggregations) {
    return new Min(field, subAggregations);
  }

  static Sum sum(AggregationField field, Aggregation... subAggregations) {
    return new Sum(field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Sum sum(AggregationField field, List<Aggregation> subAggregations) {
    return new Sum(field, subAggregations);
  }

  static Count count(AggregationField field, Aggregation... subAggregations) {
    return new Count(field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Count count(AggregationField field, List<Aggregation> subAggregations) {
    return new Count(field, subAggregations);
  }

  static Avg avg(AggregationField field, Aggregation... subAggregations) {
    return new Avg(field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Avg avg(AggregationField field, List<Aggregation> subAggregations) {
    return new Avg(field, subAggregations);
  }

  static Cardinality cardinality(AggregationField field, Aggregation... subAggregations) {
    return new Cardinality(field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Cardinality cardinality(AggregationField field, List<Aggregation> subAggregations) {
    return new Cardinality(field, subAggregations);
  }

  static DateHistogram dateHistogram(AggregationField field, Aggregation... subAggregations) {
    return new DateHistogram(field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static DateHistogram dateHistogram(AggregationField field, List<Aggregation> subAggregations) {
    return new DateHistogram(field, subAggregations);
  }

  static Terms terms(AggregationField field, Aggregation... subAggregations) {
    return new Terms(field, new ArrayList<>(Arrays.asList(subAggregations)));
  }

  static Terms terms(AggregationField field, List<Aggregation> subAggregations) {
    return new Terms(field, subAggregations);
  }

  static AggregationField aggregationField(String name, String field) {
    return new AggregationField(name, field);
  }

  static AggregationField aggregationHistogramField(String name, String field, String calendarInterval) {
    return new AggregationField(name, field, calendarInterval);
  }
}
