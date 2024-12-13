package com.dropchop.recyclone.model.entity.es.attr;

import com.dropchop.recyclone.base.api.model.attr.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static com.dropchop.recyclone.base.api.model.attr.AttributeMarshaller.marshall;
import static com.dropchop.recyclone.base.api.model.attr.AttributeMarshaller.unmarshall;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EsAttribute<X> implements Attribute<X> {

  public enum Type {
    str,
    bool,
    num,
    date,
    set,
    list
  }

  private Type type;

  @EqualsAndHashCode.Include
  private String name;

  private String strValue;

  private transient X value;

  public static <X> Attribute<X> toAttribute(EsAttribute<X> esAttribute) {
    Attribute<X> attribute;
    if (Type.str == esAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(esAttribute.getName(), esAttribute.getStrValue(), String.class);
    } else if (Type.bool == esAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(esAttribute.getName(), esAttribute.getStrValue(), Boolean.class);
    } else if (Type.date == esAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(esAttribute.getName(), esAttribute.getStrValue(), ZonedDateTime.class);
    } else if (Type.num == esAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(esAttribute.getName(), esAttribute.getStrValue(), BigDecimal.class);
    } else if (Type.set == esAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(esAttribute.getName(), esAttribute.getStrValue(), Set.class);
    } else if (Type.list == esAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(esAttribute.getName(), esAttribute.getStrValue(), List.class);
    } else {
      throw new UnsupportedOperationException("Unsupported JpaAttribute to Attribute conversion");
    }
    attribute.setName(esAttribute.getName());

    return attribute;
  }

  public static <X> EsAttribute<X> fromAttribute(Attribute<X> attribute) {
    if (attribute instanceof AttributeToRemove) {
      return null;
    }
    EsAttribute<X> esAttribute = new EsAttribute<>();
    esAttribute.setName(attribute.getName());
    esAttribute.setValue(attribute.getValue());
    esAttribute.setStrValue(marshall(attribute));
    switch (attribute) {
      case AttributeString ignored -> esAttribute.setType(Type.str);
      case AttributeBool ignored -> esAttribute.setType(Type.bool);
      case AttributeDate ignored -> esAttribute.setType(Type.date);
      case AttributeDecimal ignored -> esAttribute.setType(Type.num);
      case AttributeSet ignored -> esAttribute.setType(Type.set);
      case AttributeValueList<?> ignored -> esAttribute.setType(Type.list);
      default -> throw new UnsupportedOperationException("Unsupported Attribute to JpaAttribute conversion");
    }

    return esAttribute;
  }
}
