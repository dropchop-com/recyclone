package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.QueryField;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
@Getter
public class Exists extends QueryField {

  public Exists(IQueryNode parent, String fieldName) {
    super(parent, fieldName, null);
    self.put("field", this.fieldName);
    this.put("exists", self);
  }
}
