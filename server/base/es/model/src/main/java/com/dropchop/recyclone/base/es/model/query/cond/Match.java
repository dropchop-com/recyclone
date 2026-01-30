package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.es.model.query.QueryField;
import com.dropchop.recyclone.base.es.model.query.QueryObject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 01. 2026.
 */
@Getter
public class Match extends QueryField {

  public Match(IQueryNode parent, String fieldName, CharSequence prefixValue) {
    super(parent, fieldName, prefixValue.toString());
    IQueryObject field = new QueryObject();
    field.put("query", prefixValue);
    self.put(fieldName, field);
    this.put("match", self);
  }
}
