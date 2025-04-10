package com.dropchop.recyclone.base.api.model.query.operator;

import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Text;
import com.dropchop.recyclone.base.api.model.query.operator.text.Wildcard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4/9/25.
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class Match<T extends Text> extends UnaryValueOperator<T> {

  public Match() {
  }

  public Match(T value) {
    super(value);
  }

  @JsonIgnore
  public T getText() {
    return super.getValue();
  }

  @JsonIgnore
  public T getValue() {
    return super.getValue();
  }

  @JsonInclude(NON_NULL)
  public AdvancedText get$match() {
    T value = getValue();
    if (value instanceof AdvancedText advancedText) {
      return advancedText;
    }
    return null;
  }

  @JsonInclude(NON_NULL)
  public void set$match(T value) {
    super.setValue(value);
  }

  @JsonInclude(NON_NULL)
  public Phrase get$matchPhrase() {
    T value = getValue();
    if (value instanceof Phrase phrase) {
      return phrase;
    }
    return null;
  }

  @JsonInclude(NON_NULL)
  public void set$matchPhrase(T value) {
    super.setValue(value);
  }

  @JsonInclude(NON_NULL)
  public Wildcard get$matchWildcard() {
    T value = getValue();
    if (value instanceof Wildcard wildcard) {
      return wildcard;
    }
    return null;
  }

  @JsonInclude(NON_NULL)
  public void set$matchWildcard(T value) {
    super.setValue(value);
  }
}
