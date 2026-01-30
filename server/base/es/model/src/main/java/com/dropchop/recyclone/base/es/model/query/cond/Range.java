package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.api.model.query.operator.*;
import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.QueryField;
import com.dropchop.recyclone.base.es.model.query.QueryObject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
@Getter
public class Range extends QueryField {

  private final String operator;
  private final Object value2;
  private final String operator2;

  public <T> Range(IQueryNode parent, String field, String op1, T val1, String op2, T val2) {
    super(parent, field, val1);
    operator = op1;
    value2 = val2;
    operator2 = op2;
    QueryObject operatorNode = new QueryObject();
    operatorNode.put(op1, val1);
    if (op2 != null && val2 != null) {
      operatorNode.put(op2, val2);
    }
    self.put(field, operatorNode);
    this.put("range", self);
  }

  public <T> Range(IQueryNode parent, String field, Gt<T> op) {
    this(
        parent, field,
        Gt.class.getSimpleName().toLowerCase(), op.get$gt(),
        null, null
    );
  }

  public <T> Range(IQueryNode parent, String field, Gte<T> op) {
    this(
        parent, field,
        Gte.class.getSimpleName().toLowerCase(), op.get$gte(),
        null, null
    );
  }

  public <T> Range(IQueryNode parent, String field, Lt<T> op) {
    this(
        parent, field,
        Lt.class.getSimpleName().toLowerCase(), op.get$lt(),
        null, null
    );
  }

  public <T> Range(IQueryNode parent, String field, Lte<T> op) {
    this(
        parent, field,
        Lte.class.getSimpleName().toLowerCase(), op.get$lte(),
        null, null
    );
  }

  public <T> Range(IQueryNode parent, String field, ClosedInterval<T> op) {
    this(
        parent, field,
        Gte.class.getSimpleName().toLowerCase(), op.get$gte(),
        Lte.class.getSimpleName().toLowerCase(), op.get$lte()
    );
  }

  public <T> Range(IQueryNode parent, String field, ClosedOpenInterval<T> op) {
    this(
        parent, field,
        Gte.class.getSimpleName().toLowerCase(), op.get$gte(),
        Lt.class.getSimpleName().toLowerCase(), op.get$lt()
    );
  }

  public <T> Range(IQueryNode parent, String field, OpenClosedInterval<T> op) {
    this(
        parent, field,
        Gt.class.getSimpleName().toLowerCase(), op.get$gt(),
        Lte.class.getSimpleName().toLowerCase(), op.get$lte()
    );
  }

  public Range(IQueryNode parent, String field, OpenInterval<?> op) {
    this(
        parent, field,
        Gt.class.getSimpleName().toLowerCase(), op.get$gt(),
        Lt.class.getSimpleName().toLowerCase(), op.get$lt()
    );
  }
}
