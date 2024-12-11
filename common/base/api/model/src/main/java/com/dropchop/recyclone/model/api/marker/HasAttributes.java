package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.attr.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@SuppressWarnings("unused")
public interface HasAttributes {
  Set<Attribute<?>> getAttributes();
  void setAttributes(Set<Attribute<?>> attributes);

  default HasAttributes addAttribute(Attribute<?> attribute) {
    Set<Attribute<?>> attributes = getAttributes();
    if (attributes == null) {
      attributes = new LinkedHashSet<>();
      setAttributes(attributes);
    }
    attributes.add(attribute);
    return this;
  }

  default boolean removeAttribute(Attribute<?> attribute) {
    Set<Attribute<?>> attributes = getAttributes();
    if (attributes == null) {
      return false;
    }
    return attributes.remove(attribute);
  }

  default boolean removeAttribute(String name) {
    Set<Attribute<?>> attributes = getAttributes();
    if (attributes == null) {
      return false;
    }
    for (Iterator<Attribute<?>> attributeIterator = attributes.iterator(); attributeIterator.hasNext();) {
      Attribute<?> attribute = attributeIterator.next();
      if (name.equals(attribute.getName())) {
        attributeIterator.remove();
        return true;
      }
    }
    return false;
  }

  /**
   * Creates attribute object and adds it to the collection of attributes.
   * Attribute with equal name is replaced. If value is null attribute gets removed.
   * @param name attribute name
   * @param value attribute value
   * @return this for chaining.
   */
  default HasAttributes setAttributeValue(String name, String value) {
    if (value == null) {
      removeAttribute(name);
      return this;
    }
    this.addAttribute(new AttributeString(name, value));
    return this;
  }

  /**
   * see: setAttributeValue(String name, String value)
   */
  default HasAttributes setAttributeValue(String name, BigDecimal value) {
    if (value == null) {
      removeAttribute(name);
      return this;
    }
    this.addAttribute(new AttributeDecimal(name, value));
    return this;
  }

  /**
   * see: setAttributeValue(String name, String value)
   */
  default HasAttributes setAttributeValue(String name, Boolean value) {
    if (value == null) {
      removeAttribute(name);
      return this;
    }
    this.addAttribute(new AttributeBool(name, value));
    return this;
  }

  /**
   * see: setAttributeValue(String name, String value)
   */
  default HasAttributes setAttributeValue(String name, ZonedDateTime value) {
    if (value == null) {
      removeAttribute(name);
      return this;
    }
    this.addAttribute(new AttributeDate(name, value));
    return this;
  }

  /**
   * see: setAttributeValue(String name, String value)
   */
  default HasAttributes setAttributeValue(String name, List<?> values) {
    if (values == null) {
      removeAttribute(name);
      return this;
    }
    this.addAttribute(new AttributeValueList<>(name, values));
    return this;
  }

  /**
   * Finds attribute by name in a collection of attributes.
   * @param attributes - attributes to search
   * @param name - attribute name to search for
   * @return attribute or null if not found.
   * @param <T> expected type of attribute value.
   */
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
    T value = attribute.getValue();
    if (value == null) {
      return defaultValue;
    }
    if (defaultValue == null) {
      return value;
    }
    if (value.getClass().isAssignableFrom(defaultValue.getClass())) {
      return value;
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
