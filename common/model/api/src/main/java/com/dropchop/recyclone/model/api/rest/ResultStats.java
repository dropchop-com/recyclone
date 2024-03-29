package com.dropchop.recyclone.model.api.rest;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasAttributes;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
public interface ResultStats extends Model, HasAttributes {
  Set<Attribute<?>> getAttributes();
  void setAttributes(Set<Attribute<?>> attributes);
}
