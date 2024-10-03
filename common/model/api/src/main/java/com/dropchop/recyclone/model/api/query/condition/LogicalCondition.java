package com.dropchop.recyclone.model.api.query.condition;

import com.dropchop.recyclone.model.api.query.Condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
public abstract class LogicalCondition implements Condition {
  private List<Condition> subConditions;

  protected List<Condition> getSubConditions() {
    return subConditions;
  }

  protected void setSubConditions(List<Condition> subConditions) {
    this.subConditions = subConditions;
  }

  protected void add(Condition subCondition) {
    List<Condition> subConditions = this.getSubConditions();
    if (subConditions == null) {
      subConditions = new ArrayList<>();
      this.setSubConditions(subConditions);
    }
    subConditions.add(subCondition);
  }

  protected void add(Collection<Condition> subConditionsToAdd) {
    List<Condition> subConditions = this.getSubConditions();
    if (subConditions == null) {
      subConditions = new ArrayList<>();
      this.setSubConditions(subConditions);
    }
    subConditions.addAll(subConditionsToAdd);
  }

  protected void add(Condition ... subConditions) {
    this.add(Arrays.asList(subConditions));
  }
}
