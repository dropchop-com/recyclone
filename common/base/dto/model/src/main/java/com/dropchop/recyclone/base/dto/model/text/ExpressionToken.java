package com.dropchop.recyclone.base.dto.model.text;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ExpressionToken implements Serializable {
  private StringBuilder expression = new StringBuilder();
  private boolean must = false;
  private boolean mustNot = false;
  private boolean punct = false;
  private boolean mixedCase = false;
  private boolean upperCase = false;
  private boolean isValid = true;
  private int start = 0;
  private int end = 0;
  private List<String> errors = new LinkedList<>();
  private List<String> warnings = new LinkedList<>();
  private Map<String, Object> metaData = null;

  public ExpressionToken() {
  }

  public boolean isPrefix() {
    int len = expression.length();
    if (len <= 0) {
      return false;
    }
    return expression.substring(len - 1).equals("*");
  }

  public void setStartEnd(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public void appendAndIncEnd(char c) {
    this.expression.append(c);
    this.end++;
  }

  public void append(char c) {
    this.expression.append(c);
  }
}
