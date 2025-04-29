package com.dropchop.recyclone.base.api.model.query.operator.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Filter {
  private Include include = null;
  private Exclude exclude = null;

  public Filter(Include value) {
    this.include = value;
  }

  public Filter(Exclude value) {
    this.exclude = value;
  }

  public Filter(Include value1, Exclude value2) {
    this.include = value1;
    this.exclude = value2;
  }

}
