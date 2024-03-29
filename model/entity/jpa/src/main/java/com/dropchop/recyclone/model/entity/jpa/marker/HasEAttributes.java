package com.dropchop.recyclone.model.entity.jpa.marker;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.entity.jpa.attr.EAttribute;

import java.util.HashSet;
import java.util.Iterator;
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
  default HasAttributes addAttribute(Attribute<?> attribute) {
    Set<EAttribute<?>> eAttributes = getEAttributes();
    if (eAttributes == null) {
      eAttributes = new LinkedHashSet<>();
      setEAttributes(eAttributes);
    } else {
      removeAttribute(attribute.getName());
    }
    EAttribute<?> eAttribute = EAttribute.fromAttribute((Attribute<?>) attribute);
    if (eAttribute == null) {
      return this;
    }
    eAttributes.add(eAttribute);
    return this;
  }

  @Override
  default void setAttributes(Set<Attribute<?>> attributes) {
    Set<EAttribute<?>> eAttributes = new HashSet<>();
    for (Attribute<?> attribute : attributes) {
      EAttribute<?> eAttribute = EAttribute.fromAttribute((Attribute<?>) attribute);
      if (eAttribute == null) {
        continue;
      }
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

  default boolean removeAttribute(String name) {
    Set<EAttribute<?>> eAttributes = getEAttributes();
    if (eAttributes == null) {
      return false;
    }
    for (Iterator<EAttribute<?>> eAttributeIterator = eAttributes.iterator(); eAttributeIterator.hasNext();) {
      EAttribute<?> eAttribute = eAttributeIterator.next();
      if (name.equals(eAttribute.getName())) {
        eAttributeIterator.remove();
        return true;
      }
    }
    return false;
  }


  Set<EAttribute<?>> getEAttributes();
  void setEAttributes(Set<EAttribute<?>> attributes);
}
