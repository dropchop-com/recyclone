package com.dropchop.recyclone.base.api.model.invoke;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.base.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 12. 21.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("unused")
public class StatusMessage implements Model {
  private ErrorCode code;
  private String text;

  public StatusMessage(ErrorCode code, String text) {
    this.code = code;
    this.text = text;
    this.details = Collections.emptySet();
  }

  @EqualsAndHashCode.Exclude
  private Set<Attribute<?>> details;
}
