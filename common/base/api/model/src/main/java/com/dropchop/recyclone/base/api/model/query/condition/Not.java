package com.dropchop.recyclone.base.api.model.query.condition;

import com.dropchop.recyclone.base.api.model.query.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@SuppressWarnings("unused")
public class Not extends LogicalCondition {

  public Not() {
    this.setSubConditions(new ArrayList<>());
  }

  public Not(Condition subCondition) {
    set$not(subCondition);
  }

  public Condition get$not() {
    List<Condition> subConditions = this.getSubConditions();
    if (subConditions == null || subConditions.isEmpty()) {
      return null;
    }
    return super.getSubConditions().getFirst();
  }

  public void set$not(Condition subCondition) {
    List<Condition> subConditions = this.getSubConditions();
    if (subConditions == null) {
      subConditions = new ArrayList<>();
      this.setSubConditions(subConditions);
      subConditions.add(subCondition);
    } else {
      subConditions.set(0, subCondition);
    }
  }

  public Not not(Condition subCondition) {
    this.set$not(subCondition);
    return this;
  }
}
