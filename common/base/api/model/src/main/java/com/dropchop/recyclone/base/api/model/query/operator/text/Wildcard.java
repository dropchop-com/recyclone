package com.dropchop.recyclone.base.api.model.query.operator.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@SuppressWarnings("unused")
public class Wildcard extends Text {

  @JsonInclude(NON_NULL)
  private Boolean caseInsensitive;

  @JsonInclude(NON_NULL)
  private Float boost;

  public Wildcard() {
  }

  public Wildcard(String value) {
    super(value);
  }

  public Wildcard(String value, Boolean caseInsensitive) {
    this(value);
    this.caseInsensitive = caseInsensitive;
  }

  public Wildcard(String value, Float boost) {
    super(value);
    this.boost = boost;
  }

  public Wildcard(String value, Boolean caseSensitive, Float boost) {
    this(value, caseSensitive);
    this.boost = boost;
  }
}
