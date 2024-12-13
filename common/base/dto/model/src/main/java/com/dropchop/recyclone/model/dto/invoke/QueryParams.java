package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;
import com.dropchop.recyclone.base.api.model.query.condition.And;
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
public class QueryParams extends Params implements com.dropchop.recyclone.base.api.model.invoke.QueryParams<
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

  @ToString.Include
  private AggregationList aggregation;

  @Override
  public String toString() {
    return super.toString() + ":" + getCondition() + ":" + getAggregation();
  }
}
