package com.dropchop.recyclone.model.api.query;

import java.util.HashMap;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
public class Field<T> extends HashMap<String, T> implements Condition {
  private String name;

  public Field() {
  }

  public Field(String name) {
    this.name = name;
  }

  public Field(String name, T val) {
    this.put(name, val);
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void set(T val) {
    if (this.name != null) {
      this.put(this.name, val);
    }
    throw new IllegalStateException("Missing name for setting the value!");
  }

  public void set(String name, T val) {
    if (this.name != null) {
      this.remove(this.name, val);
    }
    this.put(name, val);
    this.name = name;
  }
}
