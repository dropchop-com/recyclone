package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;
import lombok.Getter;

import java.util.HashMap;

import static com.dropchop.recyclone.model.api.query.Aggregation.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 10. 24.
 */
@Getter
@SuppressWarnings("unused")
public class AggregationContainer extends HashMap<String, Aggregation> implements Aggregation {
  private String name;

  public AggregationContainer(Aggregation val) {
    this.name = computeName(val);
    this.put(name, val);
  }

  public AggregationContainer() {
  }

  public void set(Aggregation val) {
    this.name = computeName(val);
    this.put(this.name, val);
  }
}
