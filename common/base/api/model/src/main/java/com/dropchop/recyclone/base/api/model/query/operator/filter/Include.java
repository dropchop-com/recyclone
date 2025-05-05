package com.dropchop.recyclone.base.api.model.query.operator.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Include {
  private List<String> value;
}
