package com.dropchop.recyclone.model.dto.rest;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 12. 21.
 */
@Data
public class ResultStats implements Model, HasAttributes {
  @EqualsAndHashCode.Exclude
  private Set<Attribute<?>> attributes;
}
