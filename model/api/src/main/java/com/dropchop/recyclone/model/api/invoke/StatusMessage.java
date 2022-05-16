package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.base.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 12. 21.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusMessage implements Model {
  private ErrorCode code;
  private String text;

  @EqualsAndHashCode.Exclude
  private Set<Attribute<?>> details;
}
