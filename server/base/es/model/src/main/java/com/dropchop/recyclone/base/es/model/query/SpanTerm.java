package com.dropchop.recyclone.base.es.model.query;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 01. 2026.
 */
public class SpanTerm extends QueryObject {

  @SuppressWarnings("FieldCanBeLocal")
  private final IQueryObject self = new QueryObject(this);

  public SpanTerm(IQueryNode parent, String fieldName, CharSequence termValue) {
    super(parent);
    IQueryObject field = new QueryObject(this);
    field.put("value", termValue);
    self.put(fieldName, field);
    this.put("span_term", self);
  }
}
