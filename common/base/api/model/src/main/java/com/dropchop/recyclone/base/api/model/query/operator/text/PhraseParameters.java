package com.dropchop.recyclone.base.api.model.query.operator.text;

import com.dropchop.recyclone.base.api.model.query.Condition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhraseParameters<T> implements Condition {
  private T name;
  private T value;

  // Optional parameters
  private String analyzer;
  private Integer slop;

  public PhraseParameters() {

  }

  public PhraseParameters(T name, T value) {
    this.name = name;
    this.value = value;
  }

  public PhraseParameters(T name, T value, String analyzer) {
    this.name = name;
    this.value = value;
    this.analyzer = analyzer;
  }

  public PhraseParameters(T name, T value, Integer slop) {
    this.name = name;
    this.value = value;
    this.slop = slop;
  }

  public PhraseParameters(T name, T value, String analyzer, Integer slop) {
    this.name = name;
    this.value = value;
    this.analyzer = analyzer;
    this.slop = slop;
  }
}
