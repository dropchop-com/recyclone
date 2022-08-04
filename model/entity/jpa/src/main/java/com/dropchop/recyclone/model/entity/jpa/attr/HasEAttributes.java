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
  @Override
  default Set<Attribute<?>> getAttributes() {
    Set<Attribute<?>> attributes = new LinkedHashSet<>();
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

  @Override
  default void addAttribute(Attribute<?> attribute) {
    Set<EAttribute<?>> eAttributes = getEAttributes();
    if (eAttributes == null) {
      eAttributes = new LinkedHashSet<>();
      setEAttributes(eAttributes);
    }
    EAttribute<?> eAttribute = EAttribute.fromAttribute((Attribute<?>) attribute);
    eAttributes.add(eAttribute);
  }

  @Override
  default void setAttributes(Set<Attribute<?>> attributes) {
    Set<EAttribute<?>> eAttributes = new HashSet<>();
    for (Attribute<?> attribute : attributes) {
      EAttribute<?> eAttribute = EAttribute.fromAttribute((Attribute<?>) attribute);
      eAttributes.add(eAttribute);
    }
    setEAttributes(eAttributes);
  }

  default <X> X getAttributeValue(String name, X defaultValue) {
    Set<EAttribute<?>> eAttributes = getEAttributes();
    if (eAttributes == null) {
      return defaultValue;
    }
    if (name == null) {
      return defaultValue;
    }
    for (EAttribute<?> eAttribute : eAttributes) {
      String attrName = eAttribute.getName();
      if (name.equals(attrName)) {
        Attribute<?> attribute = EAttribute.toAttribute(eAttribute);
        //noinspection unchecked
        return HasAttributes.getValueChecked((Attribute<X>)attribute, defaultValue);
      }
    }
    return defaultValue;
  }

  default <X> X getAttributeValue(String name) {
    return getAttributeValue(name, null);
  }

  default <X> Attribute<X> getAttribute(String name) {
    Set<EAttribute<?>> eAttributes = getEAttributes();
    if (eAttributes == null) {
      return null;
    }
    if (name == null) {
      return null;
    }
    for (EAttribute<?> eAttribute : eAttributes) {
      String attrName = eAttribute.getName();
      if (name.equals(attrName)) {
        //noinspection unchecked
        return (Attribute<X>) EAttribute.toAttribute(eAttribute);
      }
    }
    return null;
  }

  Set<EAttribute<?>> getEAttributes();
  void setEAttributes(Set<EAttribute<?>> attributes);
}
