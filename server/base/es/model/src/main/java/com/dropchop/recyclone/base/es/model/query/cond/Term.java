package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.es.model.query.QueryField;
import com.dropchop.recyclone.base.es.model.query.QueryObject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
public class Term extends QueryField {

  @SuppressWarnings("FieldCanBeLocal")
  private final IQueryObject self = new QueryObject(this);

  public Term(IQueryNode parent, String fieldName, Object value) {
    super(parent, fieldName, value);
    self.put(this.fieldName, value);
    this.put("term", self);
  }
}
