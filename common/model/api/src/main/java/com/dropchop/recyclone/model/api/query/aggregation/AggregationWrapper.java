package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.dropchop.recyclone.model.api.query.Aggregation.computeTypeName;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 10. 24.
 */
@SuppressWarnings("unused")
public class AggregationWrapper extends HashMap<String, Aggregation> implements Aggregation {
  private Aggregation aggregation;

  public AggregationWrapper(Aggregation val) {
    this.aggregation = val;
    this.put(computeTypeName(val), val);
  }

  public AggregationWrapper() {
  }

  public void set(Aggregation val) {
    this.clear();
    this.aggregation = val;
    this.put(computeTypeName(val), val);
  }

  @Override
  public String getName() {
    return this.aggregation.getName();
  }

  @Override
  public String getField() {
    return this.aggregation.getField();
  }

  public static AggregationList aggs(Aggregation ... aggs) {
    return Arrays.stream(aggs).map(AggregationWrapper::new).collect(Collectors.toCollection(AggregationList::new));
  }

  public static AggregationList aggs(List<Aggregation> aggs) {
    return aggs.stream().map(AggregationWrapper::new).collect(Collectors.toCollection(AggregationList::new));
  }

  public static Max max(String name, String field, Aggregation... aggs) {
    return new Max(name, field, Arrays.stream(aggs).map(AggregationWrapper::new)
        .collect(Collectors.toCollection(AggregationList::new)));
  }

  public static Min min(String name, String field, Aggregation... aggs) {
    return new Min(name, field, Arrays.stream(aggs).map(AggregationWrapper::new)
        .collect(Collectors.toCollection(AggregationList::new)));
  }

  public static Sum sum(String name, String field, Aggregation... aggs) {
    return new Sum(name, field, Arrays.stream(aggs).map(AggregationWrapper::new)
        .collect(Collectors.toCollection(AggregationList::new)));
  }

  public static Count count(String name, String field, Aggregation... aggs) {
    return new Count(name, field, Arrays.stream(aggs).map(AggregationWrapper::new)
        .collect(Collectors.toCollection(AggregationList::new)));
  }

  public static Avg avg(String name, String field, Aggregation... aggs) {
    return new Avg(name, field, Arrays.stream(aggs).map(AggregationWrapper::new)
        .collect(Collectors.toCollection(AggregationList::new)));
  }

  public static Cardinality cardinality(String name, String field, Aggregation... aggs) {
    return new Cardinality(name, field, Arrays.stream(aggs).map(AggregationWrapper::new)
        .collect(Collectors.toCollection(AggregationList::new)));
  }

  public static DateHistogram dateHistogram(String name, String field, String calendar_interval, Aggregation... aggs) {
    return new DateHistogram(
        name, field, calendar_interval, Arrays.stream(aggs).map(AggregationWrapper::new)
        .collect(Collectors.toCollection(AggregationList::new))
    );
  }

  public static Terms terms(String name, String field, Aggregation... aggs) {
    return new Terms(name, field, Arrays.stream(aggs).map(AggregationWrapper::new)
        .collect(Collectors.toCollection(AggregationList::new)));
  }
}
