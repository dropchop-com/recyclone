package com.dropchop.recyclone.base.api.model.query.condition;

import com.dropchop.recyclone.base.api.model.query.Condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@SuppressWarnings("unused")
public class Or extends LogicalCondition {

  public Or() {
    this.setSubConditions(new ArrayList<>());
  }

  public Or(List<Condition> or) {
    this.setSubConditions(or);
  }

  public List<Condition> get$or() {
    return super.getSubConditions();
  }

  public void set$or(List<Condition> subConditions) {
    super.setSubConditions(subConditions);
  }

  public Or or(Condition subCondition) {
    super.add(subCondition);
    return this;
  }

  public Or or(Collection<Condition> subConditions) {
    super.add(subConditions);
    return this;
  }

  public Or or(Condition ... subCondition) {
    this.add(subCondition);
    return this;
  }
}
