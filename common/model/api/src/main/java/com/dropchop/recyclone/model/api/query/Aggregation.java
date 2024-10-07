package com.dropchop.recyclone.model.api.query;

import com.dropchop.recyclone.model.api.query.aggregation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
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

  static String computeTypeName(Aggregation aggregation) {
    String name = aggregation.getClass().getSimpleName();
    return "$" + name.substring(0, 1).toLowerCase() + name.substring(1);
  }

  static AggregationList aggs(Aggregation ... aggs) {
    return Arrays.stream(aggs).map(AggregationWrapper::new).collect(Collectors.toCollection(AggregationList::new));
  }

  static AggregationList aggs(List<Aggregation> aggs) {
    return aggs.stream().map(AggregationWrapper::new).collect(Collectors.toCollection(AggregationList::new));
  }

  static Max max(String name, String field, Aggregation... aggs) {
    return new Max(name, field, aggs);
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

  static DateHistogram dateHistogram(String name, String field, String calendar_interval, Aggregation... subAggregations) {
    return new DateHistogram(name, field, calendar_interval, subAggregations);
  }

  static Terms terms(String name, String field, Aggregation... subAggregations) {
    return new Terms(name, field, subAggregations);
  }

  String getName();
  String getField();
}
