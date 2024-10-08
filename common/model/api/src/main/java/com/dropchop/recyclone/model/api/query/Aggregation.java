package com.dropchop.recyclone.model.api.query;

import com.dropchop.recyclone.model.api.query.aggregation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Base interface that marks aggregation description bearing object.
 * This interface supports also the fluent api methods for Aggregation tree construction.
 * For example:
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

  /**
   * Base interface that marks aggregation description wrapper object to support correct JSON serialization.
   * This interface supports also the fluent api for Aggregation tree construction and should be used if
   * aggregation tree is to be serialized to JSON w/o special type information as it wraps each aggregation
   * in a aggregation type information object.
   * For example:
   * {
   *   "aggs": [],
   *   "name": "nested_worker_sum",
   *   "field": "worker"
   * }
   * is wrapped in to:
   * {
   *   "$sum": {
   *     "aggs": [],
   *     "name": "nested_worker_sum",
   *     "field": "worker"
   *   }
   * },
   * If you have unwrapped version of the tree you can wrap it with the Wrapper.wrap() method.
   * This interface supports also the fluent api methods for Aggregation tree construction.
   * For example:
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

    public static Max max(String name, String field, Aggregation... aggs) {
      return new Max(name, field, Arrays.stream(aggs).map(Wrapper::new)
          .collect(Collectors.toCollection(AggregationList::new)));
    }

    public static Min min(String name, String field, Aggregation... aggs) {
      return new Min(name, field, Arrays.stream(aggs).map(Wrapper::new)
          .collect(Collectors.toCollection(AggregationList::new)));
    }

    public static Sum sum(String name, String field, Aggregation... aggs) {
      return new Sum(name, field, Arrays.stream(aggs).map(Wrapper::new)
          .collect(Collectors.toCollection(AggregationList::new)));
    }

    public static Count count(String name, String field, Aggregation... aggs) {
      return new Count(name, field, Arrays.stream(aggs).map(Wrapper::new)
          .collect(Collectors.toCollection(AggregationList::new)));
    }

    public static Avg avg(String name, String field, Aggregation... aggs) {
      return new Avg(name, field, Arrays.stream(aggs).map(Wrapper::new)
          .collect(Collectors.toCollection(AggregationList::new)));
    }

    public static Cardinality cardinality(String name, String field, Aggregation... aggs) {
      return new Cardinality(name, field, Arrays.stream(aggs).map(Wrapper::new)
          .collect(Collectors.toCollection(AggregationList::new)));
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
