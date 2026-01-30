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
public class SpanTerm extends QueryField {

  public SpanTerm(IQueryNode parent, String fieldName, CharSequence termValue) {
    super(parent, fieldName, termValue.toString());
    IQueryObject field = new QueryObject(this);
    field.put("value", this.value);
    self.put(this.fieldName, field);
    this.put("span_term", self);
  }
}
