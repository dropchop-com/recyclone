package com.dropchop.recyclone.base.es.model.query;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 01. 2026.
 */
public class Match extends QueryObject {

  @SuppressWarnings("FieldCanBeLocal")
  private final IQueryObject self = new QueryObject(this);

  public Match(IQueryNode parent, String fieldName, CharSequence prefixValue) {
    super(parent);
    IQueryObject field = new QueryObject(this);
    field.put("query", prefixValue);
    self.put(fieldName, field);
    this.put("match", self);
  }
}
