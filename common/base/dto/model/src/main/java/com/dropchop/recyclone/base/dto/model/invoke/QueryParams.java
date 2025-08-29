package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;
import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.condition.Or;
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

  @SuppressWarnings("unused")
  public abstract static class QueryParamsBuilder<C extends QueryParams, B extends QueryParamsBuilder<C, B>> extends Params.ParamsBuilder<C, B> {
    public B and(Condition ... condition) {
      if (this.condition == null) {
        this.condition = com.dropchop.recyclone.base.api.model.query.Condition.and();
      }
      if (this.condition instanceof And and) {
        for (Condition c : condition) {
          and.get$and().add(c);
        }
      }
      return self();
    }

    public B or(Condition ... condition) {
      if (this.condition == null) {
        this.condition = com.dropchop.recyclone.base.api.model.query.Condition.or();
      }
      if (this.condition instanceof Or or) {
        for (Condition c : condition) {
          or.get$or().add(c);
        }
      }
      return self();
    }

    public B not(Condition condition) {
      condition(com.dropchop.recyclone.base.api.model.query.Condition.not(condition));
      return self();
    }

    public B aggs(Aggregation... aggs) {
      aggregate(com.dropchop.recyclone.base.api.model.query.Aggregation.aggs(aggs));
      return self();
    }

    public B agg(Aggregation... aggs) {
      if (aggregate == null) {
        aggregate = new AggregationList();
      }
      aggregate.addAll(com.dropchop.recyclone.base.api.model.query.Aggregation.aggs(aggs));
      return self();
    }
  }

  @Override
  @JsonIgnore
  public ResultFilterDefaults getFilterDefaults() {
    return new ResultFilterDefaults();
  }

  @ToString.Include
  private Condition condition = new And();

  @ToString.Include
  private AggregationList aggregate;

  @Override
  public String toString() {
    return super.toString() + ":" + getCondition() + ":" + this.getAggregate();
  }
}
