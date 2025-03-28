package com.dropchop.recyclone.base.api.model.query.condition;

import com.dropchop.recyclone.base.api.model.query.Condition;

import java.util.*;

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

  public Iterator<Condition> iterator() {
    return this.getSubConditions().iterator();
  }
}
