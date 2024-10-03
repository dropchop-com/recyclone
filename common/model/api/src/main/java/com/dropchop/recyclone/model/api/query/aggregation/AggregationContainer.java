package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.HashMap;

import static com.dropchop.recyclone.model.api.query.Aggregation.computeTypeName;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 10. 24.
 */
@SuppressWarnings("unused")
public class AggregationContainer extends HashMap<String, Aggregation> implements Aggregation {
  private Aggregation aggregation;

  public AggregationContainer(Aggregation val) {
    this.aggregation = val;
    this.put(computeTypeName(val), val);
  }

  public AggregationContainer() {
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
}
