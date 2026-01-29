package com.dropchop.recyclone.base.api.model.query.operator.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Getter
@Setter
@SuppressWarnings("unused")
@JsonInclude(NON_NULL)
public class AdvancedText extends Text {

  private Boolean caseInsensitive = null;

  private Integer slop = null;

  private Boolean inOrder = null;

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
