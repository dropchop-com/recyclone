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

  default <T> Attribute<T> getAttribute(Set<Attribute<?>> attributes, String name) {
    if (attributes == null) {
      return null;
    }
    if (name == null) {
      return null;
    }
    for (Attribute<?> attribute : attributes) {
      String attrName = attribute.getName();
      if (name.equals(attrName)) {
        //noinspection unchecked
        return (Attribute<T>) attribute;
      }
    }
    return null;
  }

  static <T> T getValueChecked(Attribute<T> attribute, T defaultValue) {
    Object value = attribute.getValue();
    if (value == null) {
      return defaultValue;
    }
    if (defaultValue == null) {
      //noinspection unchecked
      return (T)value;
    }
    if (value.getClass().isAssignableFrom(defaultValue.getClass())) {
      //noinspection unchecked
      return (T)value;
    } else {
      throw new IllegalArgumentException("Attribute is wrong type [" + value.getClass()
        + "], expected [" + defaultValue.getClass() + "]");
    }
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
        //noinspection unchecked
        return getValueChecked((Attribute<T>)attribute, defaultValue);
      }
    }
    return defaultValue;
  }

  static <T> T getAttributeValue(Set<Attribute<?>> attributes, String name) {
    return getAttributeValue(attributes, name, null);
  }

  default <T> T getAttributeValue(String name, T defaultValue) {
    return getAttributeValue(getAttributes(), name, defaultValue);
  }

  default <T> T getAttributeValue(String name) {
    return getAttributeValue(name, null);
  }

  default <T> Attribute<T> getAttribute(String name) {
    return getAttribute(getAttributes(), name);
  }
}
