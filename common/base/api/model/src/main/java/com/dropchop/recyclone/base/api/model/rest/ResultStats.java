package com.dropchop.recyclone.base.api.model.rest;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 22.
 */
public interface ResultStats extends Model, HasAttributes {
  Set<Attribute<?>> getAttributes();
  void setAttributes(Set<Attribute<?>> attributes);
}
