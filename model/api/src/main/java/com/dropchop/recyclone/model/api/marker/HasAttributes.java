package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.attr.Attribute;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
public interface HasAttributes {
  Set<Attribute<?>> getAttributes();
  void setAttributes(Set<Attribute<?>> attributes);

  default void addAttribute(Attribute<?> attribute) {
    Set<Attribute<?>> attributes = getAttributes();
    if (attributes == null) {
      attributes = new LinkedHashSet<>();
      setAttributes(attributes);
    }
    attributes.add(attribute);
  }

  static <T> T getAttributeValue(Set<Attribute<?>> attributes, String name, T defaultValue) {
    if (attributes == null) {
      return defaultValue;
    }
    if (name == null) {
      return defaultValue;
    }
    for (Attribute<?> attribute : attributes) {
      String attrName = attribute.getName();
      if (name.equals(attrName)) {
        Object value = attribute.getValue();
        if (defaultValue == null) {
          //noinspection unchecked
          return (T)attribute.getValue();
        }
        if (defaultValue.getClass().isAssignableFrom(value.getClass())) {
          //noinspection unchecked
          return (T)attribute.getValue();
        } else {
          throw new IllegalArgumentException("Attribute is wrong type [" + value.getClass()
            + "], expected [" + defaultValue.getClass() + "]");
        }
      }
    }
    return defaultValue;
  }

  default <T> T getAttributeValue(String name, T defaultValue) {
    return getAttributeValue(getAttributes(), name, defaultValue);
  }

  default <T> T getAttributeValue(String name) {
    return getAttributeValue(name, null);
  }
}
