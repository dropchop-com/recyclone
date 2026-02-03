package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import lombok.Getter;

/**
 * Base field-backed aggregation node.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
@Getter
public abstract class AggregationField extends Aggregation {
  private String field;

  protected AggregationField(IQueryNode parent, String type, String field) {
    super(parent, type);
    setField(field);
  }

  public void setField(String field) {
    this.field = field;
    if (field == null) {
      body.remove("field");
    } else {
      body.put("field", field);
    }
  }
}
