package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.query.Condition;
import com.dropchop.recyclone.model.api.query.aggregation.AggregationWrapper;
import com.dropchop.recyclone.model.api.query.aggregation.AggregationWrappers;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@SuppressWarnings("unused")
public interface QueryParams<
  RF extends ResultFilter<CF, LF>,
  CF extends ResultFilter.ContentFilter,
  LF extends ResultFilter.LanguageFilter,
  FD extends ResultFilterDefaults> extends CommonParams<RF, CF, LF, FD> {

  Condition getCondition();
  void setCondition(Condition condition);

  AggregationWrappers getAggregation();
  void setAggregation(AggregationWrappers aggregations);

  default Condition condition() {
    return getCondition();
  }

  default QueryParams<RF, CF, LF, FD> condition(Condition condition) {
    setCondition(condition);
    return this;
  }
}
