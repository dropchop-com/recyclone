package com.dropchop.recyclone.quarkus.deployment.invoke;

import java.util.Objects;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 04. 24.
 */
public class ContextMapping {
  final String contextClass;
  final String dataClass;
  final Integer priority;

  public ContextMapping(String contextClass, String dataClass, Integer priority) {
    this.contextClass = contextClass;
    this.dataClass = dataClass;
    this.priority = priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ContextMapping that)) return false;
    return Objects.equals(contextClass, that.contextClass) && Objects.equals(dataClass, that.dataClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contextClass, dataClass);
  }
}
