package com.dropchop.recyclone.base.api.model.query.operator.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Getter
@Setter
@SuppressWarnings("unused")
public class AdvancedText extends Text {

  @JsonInclude(NON_NULL)
  private Boolean caseInsensitive = null;

  private Integer slop = 0;

  private Boolean inOrder = true;

  public AdvancedText() {
  }

  public AdvancedText(String value) {
    super(value);
  }

  public AdvancedText(String value, Integer slop) {
    this(value);
    this.slop = slop;
  }

  public AdvancedText(String value, Boolean caseInsensitive) {
    this(value);
    this.caseInsensitive = caseInsensitive;
  }

  public AdvancedText(String value, Boolean caseInsensitive, Integer slop) {
    this(value, caseInsensitive);
    this.slop = slop;
  }

  public AdvancedText(String value, Boolean caseInsensitive, Integer slop, Boolean inOrder) {
    this(value, caseInsensitive, slop);
    this.inOrder = inOrder;
  }
}
