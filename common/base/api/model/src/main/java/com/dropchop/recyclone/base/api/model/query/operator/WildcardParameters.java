package com.dropchop.recyclone.base.api.model.query.operator;


import com.dropchop.recyclone.base.api.model.query.Condition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WildcardParameters<T> implements Condition {
  private T name;
  private T value;

  // Default values of optional parameters
  private Boolean caseInsensitive = false;
  private Float boost = 1.0f;

  public WildcardParameters() {

  }

  public WildcardParameters(T name, T value) {
    this.name = name;
    this.value = value;
  }

  public WildcardParameters(T name, T value, Boolean caseInsensitive) {
    this.name = name;
    this.value = value;
    this.caseInsensitive = caseInsensitive;
  }

  public WildcardParameters(T name, T value, Float boost) {
    this.name = name;
    this.value = value;
    this.boost = boost;
  }

  public WildcardParameters(T name, T value, Boolean caseInsensitive, Float boost) {
    this.name = name;
    this.value = value;
    this.caseInsensitive = caseInsensitive;
    this.boost = boost;
  }
}
