package com.dropchop.recyclone.base.es.model.query;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 01. 2026.
 */
@SuppressWarnings("unused")
public class Prefix extends QueryObject {

  @SuppressWarnings("FieldCanBeLocal")
  private final IQueryObject self = new QueryObject(this);

  public Prefix(IQueryNode parent, String fieldName, CharSequence prefixValue, Boolean caseInsensitive) {
    super(parent);
    IQueryObject field = new QueryObject(this);
    field.put("value", prefixValue);
    if (caseInsensitive != null) {
      field.put("case_insensitive", caseInsensitive);
    }
    self.put(fieldName, field);
    this.put("prefix", self);
  }

  public Prefix(IQueryNode parent, String fieldName, CharSequence prefixValue) {
    this(parent, fieldName, prefixValue, null);
  }
}
