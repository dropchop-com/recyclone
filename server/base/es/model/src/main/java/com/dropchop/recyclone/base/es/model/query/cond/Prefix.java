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
@SuppressWarnings("unused")
public class Prefix extends QueryField {

  private final Boolean caseInsensitive;

  public Prefix(IQueryNode parent, String fieldName, CharSequence prefixValue, Boolean caseInsensitive) {
    super(parent, fieldName, prefixValue.toString());
    this.caseInsensitive = caseInsensitive;
    IQueryObject field = new QueryObject(this);
    field.put("value", this.value);
    if (caseInsensitive != null) {
      field.put("case_insensitive", caseInsensitive);
    }
    self.put(this.fieldName, field);
    this.put("prefix", self);
  }

  public Prefix(IQueryNode parent, String fieldName, CharSequence prefixValue) {
    this(parent, fieldName, prefixValue, null);
  }
}
