package com.dropchop.recyclone.base.es.model.query;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 01. 2026.
 */
@SuppressWarnings("unused")
public class Wildcard extends QueryObject {

  @SuppressWarnings("FieldCanBeLocal")
  private final IQueryObject self = new QueryObject(this);

  public Wildcard(IQueryNode parent, String fieldName, CharSequence prefixValue, Boolean caseInsensitive, Float boost) {
    super(parent);
    IQueryObject field = new QueryObject(this);
    field.put("value", prefixValue);
    if (caseInsensitive != null) {
      field.put("case_insensitive", caseInsensitive);
    }
    if (boost != null) {
      field.put("boost", boost);
    }
    self.put(fieldName, field);
    this.put("wildcard", self);
  }

  public Wildcard(IQueryNode parent, String fieldName, CharSequence prefixValue, Boolean caseInsensitive) {
    this(parent, fieldName, prefixValue, caseInsensitive, null);
  }

  public Wildcard(IQueryNode parent, String fieldName, CharSequence prefixValue) {
    this(parent, fieldName, prefixValue, null, null);
  }
}
