package com.dropchop.recyclone.model.entity.jpa.marker;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.entity.jpa.attr.JpaAttribute;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 25. 07. 22.
 */
public interface HasJpaAttributes extends HasAttributes {
  @Override
  default Set<Attribute<?>> getAttributes() {
    Set<Attribute<?>> attributes = new LinkedHashSet<>();
    Set<JpaAttribute<?>> jpaAttributes = getJpaAttributes();
    if (jpaAttributes == null) {
      return attributes;
    }

    for (JpaAttribute<?> jpaAttribute : jpaAttributes) {
      Attribute<?> attribute = JpaAttribute.toAttribute(jpaAttribute);
      attributes.add(attribute);
    }

    return attributes;
  }

  @Override
  default HasAttributes addAttribute(Attribute<?> attribute) {
    Set<JpaAttribute<?>> jpaAttributes = getJpaAttributes();
    if (jpaAttributes == null) {
      jpaAttributes = new LinkedHashSet<>();
      setJpaAttributes(jpaAttributes);
    } else {
      removeAttribute(attribute.getName());
    }
    JpaAttribute<?> jpaAttribute = JpaAttribute.fromAttribute((Attribute<?>) attribute);
    if (jpaAttribute == null) {
      return this;
    }
    jpaAttributes.add(jpaAttribute);
    return this;
  }

  @Override
  default void setAttributes(Set<Attribute<?>> attributes) {
    Set<JpaAttribute<?>> jpaAttributes = new HashSet<>();
    for (Attribute<?> attribute : attributes) {
      JpaAttribute<?> jpaAttribute = JpaAttribute.fromAttribute((Attribute<?>) attribute);
      if (jpaAttribute == null) {
        continue;
      }
      jpaAttributes.add(jpaAttribute);
    }
    setJpaAttributes(jpaAttributes);
  }

  default <X> X getAttributeValue(String name, X defaultValue) {
    Set<JpaAttribute<?>> jpaAttributes = getJpaAttributes();
    if (jpaAttributes == null) {
      return defaultValue;
    }
    if (name == null) {
      return defaultValue;
    }
    for (JpaAttribute<?> jpaAttribute : jpaAttributes) {
      String attrName = jpaAttribute.getName();
      if (name.equals(attrName)) {
        Attribute<?> attribute = JpaAttribute.toAttribute(jpaAttribute);
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
    Set<JpaAttribute<?>> jpaAttributes = getJpaAttributes();
    if (jpaAttributes == null) {
      return null;
    }
    if (name == null) {
      return null;
    }
    for (JpaAttribute<?> jpaAttribute : jpaAttributes) {
      String attrName = jpaAttribute.getName();
      if (name.equals(attrName)) {
        //noinspection unchecked
        return (Attribute<X>) JpaAttribute.toAttribute(jpaAttribute);
      }
    }
    return null;
  }

  default boolean removeAttribute(String name) {
    Set<JpaAttribute<?>> jpaAttributes = getJpaAttributes();
    if (jpaAttributes == null) {
      return false;
    }
    for (Iterator<JpaAttribute<?>> eAttributeIterator = jpaAttributes.iterator(); eAttributeIterator.hasNext();) {
      JpaAttribute<?> jpaAttribute = eAttributeIterator.next();
      if (name.equals(jpaAttribute.getName())) {
        eAttributeIterator.remove();
        return true;
      }
    }
    return false;
  }


  Set<JpaAttribute<?>> getJpaAttributes();
  void setJpaAttributes(Set<JpaAttribute<?>> attributes);
}
