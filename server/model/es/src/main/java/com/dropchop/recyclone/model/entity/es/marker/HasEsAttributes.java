package com.dropchop.recyclone.model.entity.es.marker;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
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
    Set<EsAttribute<?>> jpaAttributes = getEsAttributes();
    if (jpaAttributes == null) {
      return attributes;
    }

    for (EsAttribute<?> jpaAttribute : jpaAttributes) {
      Attribute<?> attribute = EsAttribute.toAttribute(jpaAttribute);
      attributes.add(attribute);
    }

    return attributes;
  }

  @Override
  default HasAttributes addAttribute(Attribute<?> attribute) {
    Set<EsAttribute<?>> jpaAttributes = getEsAttributes();
    if (jpaAttributes == null) {
      jpaAttributes = new LinkedHashSet<>();
      setEsAttributes(jpaAttributes);
    } else {
      removeAttribute(attribute.getName());
    }
    EsAttribute<?> jpaAttribute = EsAttribute.fromAttribute((Attribute<?>) attribute);
    if (jpaAttribute == null) {
      return this;
    }
    jpaAttributes.add(jpaAttribute);
    return this;
  }

  @Override
  default void setAttributes(Set<Attribute<?>> attributes) {
    Set<EsAttribute<?>> jpaAttributes = new HashSet<>();
    for (Attribute<?> attribute : attributes) {
      EsAttribute<?> jpaAttribute = EsAttribute.fromAttribute((Attribute<?>) attribute);
      if (jpaAttribute == null) {
        continue;
      }
      jpaAttributes.add(jpaAttribute);
    }
    setEsAttributes(jpaAttributes);
  }

  default <X> X getAttributeValue(String name, X defaultValue) {
    Set<EsAttribute<?>> jpaAttributes = getEsAttributes();
    if (jpaAttributes == null) {
      return defaultValue;
    }
    if (name == null) {
      return defaultValue;
    }
    for (EsAttribute<?> jpaAttribute : jpaAttributes) {
      String attrName = jpaAttribute.getName();
      if (name.equals(attrName)) {
        Attribute<?> attribute = EsAttribute.toAttribute(jpaAttribute);
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
    Set<EsAttribute<?>> jpaAttributes = getEsAttributes();
    if (jpaAttributes == null) {
      return null;
    }
    if (name == null) {
      return null;
    }
    for (EsAttribute<?> jpaAttribute : jpaAttributes) {
      String attrName = jpaAttribute.getName();
      if (name.equals(attrName)) {
        //noinspection unchecked
        return (Attribute<X>) EsAttribute.toAttribute(jpaAttribute);
      }
    }
    return null;
  }

  default boolean removeAttribute(String name) {
    Set<EsAttribute<?>> jpaAttributes = getEsAttributes();
    if (jpaAttributes == null) {
      return false;
    }
    for (Iterator<EsAttribute<?>> eAttributeIterator = jpaAttributes.iterator(); eAttributeIterator.hasNext();) {
      EsAttribute<?> jpaAttribute = eAttributeIterator.next();
      if (name.equals(jpaAttribute.getName())) {
        eAttributeIterator.remove();
        return true;
      }
    }
    return false;
  }


  Set<EsAttribute<?>> getEsAttributes();
  void setEsAttributes(Set<EsAttribute<?>> attributes);
}
