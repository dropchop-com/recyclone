package com.dropchop.recyclone.model.entity.es.marker;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;
import com.dropchop.recyclone.model.entity.es.attr.EsAttribute;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 25. 07. 22.
 */
public interface HasEsAttributes extends HasAttributes {
  @Override
  default Set<Attribute<?>> getAttributes() {
    Set<Attribute<?>> attributes = new LinkedHashSet<>();
    Set<EsAttribute<?>> esAttributes = getEsAttributes();
    if (esAttributes == null) {
      return attributes;
    }

    for (EsAttribute<?> esAttribute : esAttributes) {
      Attribute<?> attribute = EsAttribute.toAttribute(esAttribute);
      attributes.add(attribute);
    }

    return attributes;
  }

  @Override
  default HasAttributes addAttribute(Attribute<?> attribute) {
    Set<EsAttribute<?>> esAttributes = getEsAttributes();
    if (esAttributes == null) {
      esAttributes = new LinkedHashSet<>();
      setEsAttributes(esAttributes);
    } else {
      removeAttribute(attribute.getName());
    }
    EsAttribute<?> esAttribute = EsAttribute.fromAttribute((Attribute<?>) attribute);
    if (esAttribute == null) {
      return this;
    }
    esAttributes.add(esAttribute);
    return this;
  }

  @Override
  default void setAttributes(Set<Attribute<?>> attributes) {
    Set<EsAttribute<?>> esAttributes = new HashSet<>();
    for (Attribute<?> attribute : attributes) {
      EsAttribute<?> esAttribute = EsAttribute.fromAttribute((Attribute<?>) attribute);
      if (esAttribute == null) {
        continue;
      }
      esAttributes.add(esAttribute);
    }
    setEsAttributes(esAttributes);
  }

  default <X> X getAttributeValue(String name, X defaultValue) {
    Set<EsAttribute<?>> esAttributes = getEsAttributes();
    if (esAttributes == null) {
      return defaultValue;
    }
    if (name == null) {
      return defaultValue;
    }
    for (EsAttribute<?> esAttribute : esAttributes) {
      String attrName = esAttribute.getName();
      if (name.equals(attrName)) {
        Attribute<?> attribute = EsAttribute.toAttribute(esAttribute);
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
    Set<EsAttribute<?>> esAttributes = getEsAttributes();
    if (esAttributes == null) {
      return null;
    }
    if (name == null) {
      return null;
    }
    for (EsAttribute<?> esAttribute : esAttributes) {
      String attrName = esAttribute.getName();
      if (name.equals(attrName)) {
        //noinspection unchecked
        return (Attribute<X>) EsAttribute.toAttribute(esAttribute);
      }
    }
    return null;
  }

  default boolean removeAttribute(String name) {
    Set<EsAttribute<?>> esAttributes = getEsAttributes();
    if (esAttributes == null) {
      return false;
    }
    for (Iterator<EsAttribute<?>> esAttributeIterator = esAttributes.iterator(); esAttributeIterator.hasNext();) {
      EsAttribute<?> esAttribute = esAttributeIterator.next();
      if (name.equals(esAttribute.getName())) {
        esAttributeIterator.remove();
        return true;
      }
    }
    return false;
  }


  Set<EsAttribute<?>> getEsAttributes();
  void setEsAttributes(Set<EsAttribute<?>> attributes);
}
