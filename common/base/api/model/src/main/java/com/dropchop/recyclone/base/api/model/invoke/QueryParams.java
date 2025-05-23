package com.dropchop.recyclone.base.api.model.invoke;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;

import java.util.ArrayList;
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

  default List<String> getAvailableFields() {
    return new ArrayList<>();
  }

  Condition getCondition();
  void setCondition(Condition condition);

  AggregationList getAggregate();
  void setAggregate(AggregationList aggregations);

  default Condition condition() {
    return getCondition();
  }

  default QueryParams<RF, CF, LF, FD> condition(Condition condition) {
    setCondition(condition);
    return this;
  }
}
