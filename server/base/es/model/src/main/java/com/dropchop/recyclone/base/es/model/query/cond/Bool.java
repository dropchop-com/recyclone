package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.es.model.query.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
@Getter
public class Bool extends QueryObject {
  private final IQueryList must = new QueryList(this);
  private final IQueryList should = new QueryList(this);
  private final IQueryList mustNot = new QueryList(this);
  private final IQueryObject body = new QueryObject();

  @Setter
  private int minimumShouldMatch;
  @Setter
  private int numClauses;

  public Bool(IQueryNode parent, int minimumShouldMatch) {
    super(parent);
    this.minimumShouldMatch = minimumShouldMatch;
    this.put("bool", this.body);
  }

  public Bool(IQueryNode parent) {
    this(parent, 1);
  }

  public Bool() {
    this(null);
  }

  private void add(IQueryNode node, IQueryList list, String name) {
    list.add(node);
    if (list.size() == 1) {
      this.body.put(name, list.getFirst());
    } else {
      this.body.put(name, list);
    }
    numClauses++;
  }

  @Override
  public boolean isEmpty() {
    return this.must.isEmpty() && this.should.isEmpty() && this.mustNot.isEmpty();
  }

  public void must(IQueryNode node) {
    this.add(node, must, "must");
  }

  public void mustNot(IQueryNode node) {
    this.add(node, mustNot, "must_not");
  }

  public void should(IQueryNode node) {
    this.add(node, should, "should");
    if (minimumShouldMatch > -1) {
      this.body.put("minimum_should_match", minimumShouldMatch);
    }
  }
}
