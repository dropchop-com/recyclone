package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.QueryField;

import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
public class Terms extends QueryField {

  public <T> Terms(IQueryNode parent, String fieldName, Collection<T> values) {
    super(parent, fieldName, values);
    self.put(this.fieldName, values);
    this.put("terms", self);
  }
}
