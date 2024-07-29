package com.dropchop.recyclone.model.es.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryAggregation implements com.dropchop.recyclone.model.es.api.query.QueryAggregation {
  private String field;
  private AggregateType type;
}
