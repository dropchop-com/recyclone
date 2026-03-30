package com.dropchop.recyclone.base.api.model.query.operator.text;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @JsonInclude(NON_NULL)
  private Integer maxExpansions = null;

  public Phrase() {
  }

  public Phrase(String value) {
    super(value);
  }

  public Phrase(String value, Integer slop) {
    this(value);
    this.slop = slop;
  }

  public Phrase(String value, Integer slop, Integer maxExpansions) {
    this(value, slop);
    this.maxExpansions = maxExpansions;
  }

  public Phrase(String value, String analyzer) {
    this(value);
    this.analyzer = analyzer;
  }

  public Phrase(String value, String analyzer, Integer slop) {
    this(value, analyzer);
    this.slop = slop;
  }

  @JsonIgnore
  public boolean isMatchPrefix() {
    String value = getValue();
    return value != null && value.endsWith("*");
  }

  @JsonIgnore
  public String getPrefix() {
    String value = getValue();
    return value != null && value.endsWith("*") ? value.substring(0, value.length() - 1) : value;
  }
}
