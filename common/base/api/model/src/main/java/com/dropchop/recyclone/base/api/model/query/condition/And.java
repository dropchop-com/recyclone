package com.dropchop.recyclone.base.api.model.query.condition;

import com.dropchop.recyclone.base.api.model.query.Condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@SuppressWarnings("unused")
public class And extends LogicalCondition {

  public And() {
    this.setSubConditions(new ArrayList<>());
  }

  public And(List<Condition> and) {
    super.setSubConditions(and);
  }

  public List<Condition> get$and() {
    return super.getSubConditions();
  }

  public void set$and(List<Condition> subConditions) {
    super.setSubConditions(subConditions);
  }

  public And and(Condition subCondition) {
    super.add(subCondition);
    return this;
  }

  public And and(Collection<Condition> subConditionsToAdd) {
    super.add(subConditionsToAdd);
    return this;
  }

  public And and(Condition ... subCondition) {
    this.add(subCondition);
    return this;
  }
}
