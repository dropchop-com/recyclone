package com.dropchop.recyclone.base.api.model.query.operator.text;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
@JsonIgnoreProperties({"name", "value", "inOrder", "slop"})
public class AdvancedText<T> extends AdvancedTextParameters<T> {
  public AdvancedText() {
  }

  public AdvancedText(T name, T value) {
    super(name, value);
  }

  public AdvancedText(T name, T value, Integer slop) {
    super(name, value, slop);
  }

  public AdvancedText(T name, T value, Boolean inOrder) {
    super(name, value, inOrder);
  }

  public AdvancedText(T name, T value, Boolean inOrder, Integer slop) {
    super(name, value, inOrder, slop);
  }

  public AdvancedTextParameters<T> get$advancedText() {
    return new AdvancedTextParameters<>(
      super.getName(),
      super.getValue(),
      super.getInOrder(),
      super.getSlop()
    );
  }

  public void set$advancedText(AdvancedTextParameters<T> advancedText) {
    if (advancedText != null) {
      setName(advancedText.getName());
      setValue(advancedText.getValue());
      setInOrder(advancedText.getInOrder());
      setSlop(advancedText.getSlop());
    }
  }
}
