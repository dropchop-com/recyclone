package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.HasFiltering;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Filter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TopHits extends BaseAggregation implements HasFiltering {
  private Integer size;
  private List<Sort> sort;
  private Filter filter = null;

  public TopHits(String name, Integer size, List<Sort> sort, Filter filter) {
    super(name, null);
    this.size = size;
    this.sort = sort;
    this.filter = filter;
  }

  public TopHits() {
  }
}
