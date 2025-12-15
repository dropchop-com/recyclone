package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;
import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.condition.LogicalCondition;
import com.dropchop.recyclone.base.api.model.query.condition.Or;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
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

  /**
   * Custom @Singular implementation.
   */
  @SuppressWarnings("unused")
  public abstract static class QueryParamsBuilder<C extends QueryParams, B extends QueryParamsBuilder<C, B>>
      extends Params.ParamsBuilder<C, B> {

    public B and(Condition ... condition) {
      if (this.condition$value == null) {
        this.condition$value = com.dropchop.recyclone.base.api.model.query.Condition.and();
      }
      if (this.condition$value instanceof LogicalCondition cnd) {
        if (!(cnd instanceof And)) {
          if (cnd.iterator().hasNext()) {
            throw new IllegalArgumentException("Can not mix type of logical conditions!");
          }
          this.condition$value = com.dropchop.recyclone.base.api.model.query.Condition.and();
        }
      }
      if (this.condition$value instanceof And and) {
        for (Condition c : condition) {
          and.get$and().add(c);
        }
      }
      this.condition$set = true;
      return self();
    }

    public B or(Condition ... condition) {
      if (this.condition$value == null) {
        this.condition$value = com.dropchop.recyclone.base.api.model.query.Condition.or();
      }
      if (this.condition$value instanceof LogicalCondition cnd) {
        if (!(cnd instanceof Or)) {
          if (cnd.iterator().hasNext()) {
            throw new IllegalArgumentException("Can not mix type of logical conditions!");
          }
          this.condition$value = com.dropchop.recyclone.base.api.model.query.Condition.or();
        }
      }
      if (this.condition$value instanceof Or or) {
        for (Condition c : condition) {
          or.get$or().add(c);
        }
      }
      this.condition$set = true;
      return self();
    }

    public B not(Condition condition) {
      condition(com.dropchop.recyclone.base.api.model.query.Condition.not(condition));
      return self();
    }

    public B aggregate(AggregationList aggregate) {
      this.aggregate$value = aggregate;
      this.aggregate$set = true;
      return self();
    }

    public B aggs(Aggregation... aggs) {
      aggregate(com.dropchop.recyclone.base.api.model.query.Aggregation.aggs(aggs));
      return self();
    }

    public B agg(Aggregation... aggs) {
      if (this.aggregate$value == null) {
        this.aggregate$value = new AggregationList();
      }
      this.aggregate$value.addAll(com.dropchop.recyclone.base.api.model.query.Aggregation.aggs(aggs));
      this.aggregate$set = true;
      return self();
    }
  }

  @Override
  @JsonIgnore
  public ResultFilterDefaults getFilterDefaults() {
    return new ResultFilterDefaults();
  }

  @ToString.Include
  @Builder.Default
  private Condition condition = new And();

  @ToString.Include
  @Builder.Default
  private AggregationList aggregate = new AggregationList();
}
