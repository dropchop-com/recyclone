package com.dropchop.recyclone.model.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AggregationCriteria {
  private String field;
  private String type;
}
