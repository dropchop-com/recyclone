package com.dropchop.recyclone.base.api.model.query;

import com.dropchop.recyclone.base.api.model.query.aggregation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Base interface that marks aggregation description bearing object.
 * This interface supports also the fluent api methods for Aggregation tree construction.
 * For example:
 * <pre>{@code
 * AggregationList a = Aggregation.aggs(
 *       max(
 *         "watch_max",
 *         "watch",
 *         sum(
 *           "nested_worker_sum",
 *           "worker"
 *         )
 *     )
 * );
 * }</pre>
 * Do not mix Aggregation and Aggregation.Wrapper tree construction.
 * @see Wrapper
 */
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
    return Arrays.stream(aggs).map(Wrapper::new).collect(Collectors.toCollection(AggregationList::new));
  }

  static AggregationList aggs(List<Aggregation> aggs) {
    return aggs.stream().map(Wrapper::new).collect(Collectors.toCollection(AggregationList::new));
  }

  static Max max(String name, String field) {
    return new Max(name, field);
  }

  static Min min(String name, String field) {
    return new Min(name, field);
  }

  static Sum sum(String name, String field) {
    return new Sum(name, field);
  }

  static Count count(String name, String field) {
    return new Count(name, field);
  }

  static Avg avg(String name, String field) {
    return new Avg(name, field);
  }

  static Cardinality cardinality(String name, String field) {
    return new Cardinality(name, field);
  }

  static DateHistogram dateHistogram(String name, String field, String calendar_interval, Aggregation... subAggregations) {
    return new DateHistogram(name, field, calendar_interval, subAggregations);
  }

  static Terms terms(String name, String field, Aggregation... subAggregations) {
    return new Terms(name, field, subAggregations);
  }

  String getName();
  String getField();

  /**
   * Base interface that marks aggregation description wrapper object to support correct JSON serialization.
   * This interface supports also the fluent api for Aggregation tree construction and should be used if
   * aggregation tree is to be serialized to JSON w/o special type information as it wraps each aggregation
   * in a aggregation type information object.
   * For example:
   * <pre>{@code
   * {
   *   "aggs": [],
   *   "name": "nested_worker_sum",
   *   "field": "worker"
   * }
   * }</pre>
   * is wrapped in to:
   * <pre>{@code
   * {
   *   "$sum": {
   *     "aggs": [],
   *     "name": "nested_worker_sum",
   *     "field": "worker"
   *   }
   * },
   * }</pre>
   * If you have unwrapped version of the tree you can wrap it with the Wrapper.wrap() method.
   * This interface supports also the fluent api methods for Aggregation tree construction.
   * For example:
   * <pre>{@code
   * AggregationList a = Wrapper.aggs(
   *       max(
   *         "watch_max",
   *         "watch",
   *         sum(
   *           "nested_worker_sum",
   *           "worker"
   *         )
   *     )
   * );
   * }</pre>
   * Do not mix Aggregation and Aggregation.Wrapper tree construction use either one.
   * <pre>{@code
   * AggregationList unwrapped = Aggregation.aggs(...
   * AggregationList wrapped = Aggregation.Wrapper.wrap(unwrapped)
   * //or
   * AggregationList wrapped = Wrapper.aggs(...
   * }</pre>
   */
  class Wrapper extends HashMap<String, Aggregation> implements Aggregation {
    private Aggregation aggregation;

    public Wrapper(Aggregation val) {
        this.aggregation = val;
        this.put(computeTypeName(val), val);
      }

    public Wrapper() {
    }

    public void set(Aggregation val) {
      this.clear();
      this.aggregation = val;
      this.put(computeTypeName(val), val);
    }

    public static AggregationList wrap(AggregationList aggs) {
      AggregationList result = new AggregationList();
      for (Aggregation agg : aggs) {
        Wrapper wrapped = new Wrapper(agg);
        result.add(wrapped);
        if (agg instanceof BaseAggregation b) {
          b.setAggs(wrap(b.getAggs() != null ? b.getAggs() : new AggregationList()));
        }
      }
      return result;
    }

    public Iterator<Aggregation> iterator() {
      return List.of(this.aggregation).iterator();
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
      return Arrays.stream(aggs).map(Wrapper::new).collect(Collectors.toCollection(AggregationList::new));
    }

    public static AggregationList aggs(List<Aggregation> aggs) {
      return aggs.stream().map(Wrapper::new).collect(Collectors.toCollection(AggregationList::new));
    }

    public static Max max(String name, String field) {
      return new Max(name, field);
    }

    public static Min min(String name, String field) {
      return new Min(name, field);
    }

    public static Sum sum(String name, String field) {
      return new Sum(name, field);
    }

    public static Count count(String name, String field) {
      return new Count(name, field);
    }

    public static Avg avg(String name, String field) {
      return new Avg(name, field);
    }

    public static Cardinality cardinality(String name, String field) {
      return new Cardinality(name, field);
    }

    public static DateHistogram dateHistogram(String name, String field, String calendarInterval, Aggregation... aggs) {
      return new DateHistogram(
          name, field, calendarInterval, Arrays.stream(aggs).map(Wrapper::new)
          .collect(Collectors.toCollection(AggregationList::new))
      );
    }

    public static Terms terms(String name, String field, Aggregation... aggs) {
      return new Terms(name, field, Arrays.stream(aggs).map(Wrapper::new)
          .collect(Collectors.toCollection(AggregationList::new)));
    }
  }
}
