package com.dropchop.recyclone.base.dto.model.text;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Filter extends ExpressionToken implements Serializable {
  private String name = null;
}
