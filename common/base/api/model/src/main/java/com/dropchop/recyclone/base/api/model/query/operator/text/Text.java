package com.dropchop.recyclone.base.api.model.query.operator.text;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4/9/25.
 */
@Getter
@Setter
public class Text {
  private String value;

  public Text() {
  }

  public Text(String value) {
    this.value = value;
  }
}
