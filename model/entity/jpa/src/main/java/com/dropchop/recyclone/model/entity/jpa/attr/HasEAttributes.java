package com.dropchop.recyclone.model.entity.jpa.attr;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.marker.HasAttributes;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 25. 07. 22.
 */
public interface HasEAttributes extends HasAttributes {

  default Set<Attribute<?>> getAttributes() {
    Set<Attribute<?>> attributes = new HashSet<>();
    Set<EAttribute<?>> eAttributes = getEAttributes();
    if (eAttributes == null) {
      return attributes;
    }
    for (EAttribute<?> eAttribute : eAttributes) {
      Attribute<?> attribute = EAttribute.toAttribute(eAttribute);
      attributes.add(attribute);
    }
    return attributes;
  }

  default void addAttribute(Attribute<?> attribute) {
    Set<EAttribute<?>> eAttributes = getEAttributes();
    if (eAttributes == null) {
      eAttributes = new LinkedHashSet<>();
      setEAttributes(eAttributes);
    }
    EAttribute<?> eAttribute = EAttribute.fromAttribute((Attribute<?>) attribute);
    eAttributes.add(eAttribute);
  }

  default void setAttributes(Set<Attribute<?>> attributes) {
    Set<EAttribute<?>> eAttributes = new HashSet<>();
    for (Attribute<?> attribute : attributes) {
      EAttribute<?> eAttribute = EAttribute.fromAttribute((Attribute<?>) attribute);
      eAttributes.add(eAttribute);
    }
    setEAttributes(eAttributes);
  }

  Set<EAttribute<?>> getEAttributes();
  void setEAttributes(Set<EAttribute<?>> attributes);
}
