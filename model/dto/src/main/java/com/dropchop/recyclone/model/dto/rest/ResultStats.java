package com.dropchop.recyclone.model.dto.rest;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 12. 21.
 */
@Getter
@Setter
public class ResultStats implements Model, HasAttributes {
  @EqualsAndHashCode.Exclude
  private Set<Attribute<?>> attributes;
}
