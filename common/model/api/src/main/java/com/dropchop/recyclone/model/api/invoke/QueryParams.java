package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.query.Aggregation;
import com.dropchop.recyclone.model.api.query.Condition;

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

  Aggregation getAggregation();
  void setAggregation(Aggregation aggregation);

  default Condition condition() {
    return getCondition();
  }

  default QueryParams<RF, CF, LF, FD> condition(Condition condition) {
    setCondition(condition);
    return this;
  }
}
