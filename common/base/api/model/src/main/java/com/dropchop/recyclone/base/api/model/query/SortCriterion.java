package com.dropchop.recyclone.base.api.model.query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SortCriterion {
  private String field;
  private String direction;
}