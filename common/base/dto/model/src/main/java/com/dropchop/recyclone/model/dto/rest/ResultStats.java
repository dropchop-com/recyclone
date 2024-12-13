package com.dropchop.recyclone.model.dto.rest;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 12. 21.
 */
@Getter
@Setter
public class ResultStats implements com.dropchop.recyclone.base.api.model.rest.ResultStats {
  @EqualsAndHashCode.Exclude
  private Set<Attribute<?>> attributes;
}
