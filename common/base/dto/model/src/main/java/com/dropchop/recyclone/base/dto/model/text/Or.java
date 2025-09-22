package com.dropchop.recyclone.base.dto.model.text;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Or extends ExpressionToken implements Serializable {
  private List<ExpressionToken> expressionTokens = new LinkedList<>();
}
