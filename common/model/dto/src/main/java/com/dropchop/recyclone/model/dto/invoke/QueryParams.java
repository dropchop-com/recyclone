package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.model.api.query.And;
import com.dropchop.recyclone.model.api.query.Condition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class QueryParams extends Params implements com.dropchop.recyclone.model.api.invoke.QueryParams<
    ResultFilter,
    ResultFilter.ContentFilter,
    ResultFilter.LanguageFilter,
    ResultFilterDefaults> {

  @Override
  @JsonIgnore
  public ResultFilterDefaults getFilterDefaults() {
    return new ResultFilterDefaults();
  }

  @ToString.Include
  private Condition condition = new And();

  @Override
  public String toString() {
    return super.toString() + ":" + getCondition();
  }
}
