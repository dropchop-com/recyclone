package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 10. 24.
 */
@Getter
@Setter
public class BaseAggregation2 implements Aggregation {
  private List<AggregationContainer> aggs;
  private String name;
  private String field;

  public BaseAggregation2(String name, String field, Aggregation ... aggs) {
    this(name, field, Arrays.stream(aggs).map(AggregationContainer::new).toList());
  }

  public BaseAggregation2(String name, String field, List<AggregationContainer> aggs) {
    this.name = name;
    this.field = field;
    this.aggs = aggs;
  }

  public BaseAggregation2(String name, String field) {
    this.name = name;
    this.field = field;
  }

  public BaseAggregation2() {
  }
}
