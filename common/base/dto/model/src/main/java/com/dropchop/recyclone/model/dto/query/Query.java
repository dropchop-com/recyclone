package com.dropchop.recyclone.model.dto.query;

import com.dropchop.recyclone.model.api.query.AggregationCriteria;
import com.dropchop.recyclone.model.api.query.FilterCriteria;
import com.dropchop.recyclone.model.api.query.SortCriterion;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Query extends DtoCode implements com.dropchop.recyclone.model.api.query.Query {
  private int page;
  private int size;
  private List<SortCriterion> sortCriteria;
  private FilterCriteria filterCriteria;
  private List<AggregationCriteria> aggregationCriteria;

  @Override
  public void setSortCriteria(List<SortCriterion> sortCriteria) {
    this.sortCriteria = sortCriteria;
  }

  @Override
  public List<AggregationCriteria> getAggregations() {
    return aggregationCriteria;
  }

  @Override
  public void setAggregations(List<AggregationCriteria> aggregations) {
    this.aggregationCriteria = aggregations;
  }
}
