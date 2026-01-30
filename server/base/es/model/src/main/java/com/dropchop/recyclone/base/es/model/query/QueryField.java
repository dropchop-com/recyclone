package com.dropchop.recyclone.base.es.model.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
@Getter
public class QueryField extends QueryObject {

  @JsonIgnore
  @SuppressWarnings("FieldCanBeLocal")
  protected final IQueryObject self;

  @JsonIgnore
  protected final Object value;

  @JsonIgnore
  protected final String fieldName;

  protected QueryField(IQueryNode parent, String fieldName, Object value, boolean isSelf) {
    super(parent);
    this.fieldName = fieldName;
    this.value = value;
    if (isSelf) {
      this.self = this;
    } else {
      this.self = new QueryObject(this);
    }
  }

  protected QueryField(IQueryNode parent, String fieldName, Object value) {
    this(parent, fieldName, value, false);
  }
}
