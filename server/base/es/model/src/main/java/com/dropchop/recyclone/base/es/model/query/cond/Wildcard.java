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
public class Wildcard extends QueryField {

  private final Boolean caseInsensitive;
  private final Float boost;

  public Wildcard(IQueryNode parent, String fieldName, CharSequence prefixValue, Boolean caseInsensitive, Float boost) {
    super(parent, fieldName, prefixValue.toString());
    this.caseInsensitive = caseInsensitive;
    this.boost = boost;
    IQueryObject field = new QueryObject(this);
    field.put("value", prefixValue);
    if (caseInsensitive != null) {
      field.put("case_insensitive", caseInsensitive);
    }
    if (boost != null) {
      field.put("boost", boost);
    }
    body.put(fieldName, field);
    this.put("wildcard", body);
  }

  public Wildcard(IQueryNode parent, String fieldName, CharSequence prefixValue, Boolean caseInsensitive) {
    this(parent, fieldName, prefixValue, caseInsensitive, null);
  }

  public Wildcard(IQueryNode parent, String fieldName, CharSequence prefixValue) {
    this(parent, fieldName, prefixValue, null, null);
  }
}
