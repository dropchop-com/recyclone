package com.dropchop.recyclone.base.api.model.query.operator.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@SuppressWarnings("unused")
public class Phrase extends Text {

  @JsonInclude(NON_NULL)
  private String analyzer = null;

  @JsonInclude(NON_NULL)
  private Integer slop = null;

  public Phrase() {
  }

  public Phrase(String value) {
    super(value);
  }

  public Phrase(String value, Integer slop) {
    super(value);
    this.slop = slop;
  }

  public Phrase(String value, String analyzer) {
    this(value);
    this.analyzer = analyzer;
  }

  public Phrase(String value, String analyzer, Integer slop) {
    this(value, analyzer);
    this.slop = slop;
  }
}
