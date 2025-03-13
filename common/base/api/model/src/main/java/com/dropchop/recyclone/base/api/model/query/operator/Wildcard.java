package com.dropchop.recyclone.base.api.model.query.operator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
@JsonIgnoreProperties({"name", "value", "caseInsensitive", "boost"})
public class Wildcard<T> extends WildcardParameters<T> {

  public Wildcard() {
  }

  public Wildcard(T name, T value) {
    super(name, value);
  }

  public Wildcard(T name, T value, Boolean caseInsensitive) {
    super(name, value, caseInsensitive);
  }

  public Wildcard(T name, T value, Float boost) {
    super(name, value, boost);
  }

  public Wildcard(T name, T value, Boolean caseSensitive, Float boost) {
    super(name, value, caseSensitive, boost);
  }

  public WildcardParameters<T> get$wildcard() {
    return new WildcardParameters<>(
      super.getName(),
      super.getValue(),
      super.getCaseInsensitive(),
      super.getBoost()
    );
  }

  public void set$wildcard(WildcardParameters<T> wildcard) {
    if (wildcard != null) {
      setName(wildcard.getName());
      setValue(wildcard.getValue());
      setCaseInsensitive(wildcard.getCaseInsensitive());
      setBoost(wildcard.getBoost());
    }
  }
}
