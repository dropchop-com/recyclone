package com.dropchop.recyclone.model.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FilterCriteria {
  private String field;
  private String operation;
  private Object value;
}
