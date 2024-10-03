package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 10. 24.
 */
public class Max2 extends BaseAggregation2 {

  public Max2(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Max2(String name, String field, List<AggregationContainer> aggs) {
    super(name, field, aggs);
  }

  public Max2(String name, String field) {
    super(name, field);
  }

  public Max2() {
  }
}
