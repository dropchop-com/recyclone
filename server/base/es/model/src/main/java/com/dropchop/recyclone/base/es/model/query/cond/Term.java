package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.QueryField;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
public class Term extends QueryField {

  public Term(IQueryNode parent, String fieldName, Object value) {
    super(parent, fieldName, value);
    body.put(this.fieldName, value);
    this.put("term", body);
  }
}
