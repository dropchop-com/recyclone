package com.dropchop.recyclone.base.api.model.query.aggregation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TopHits extends BaseAggregation {
  private Integer size;
  private List<Sort> sort;

  public TopHits(String name, Integer size, List<Sort> sort) {
    super(name, null);
    this.size = size;
    this.sort = sort;
  }
}
