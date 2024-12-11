package com.dropchop.recyclone.model.entity.jpa.attr;

import com.dropchop.recyclone.model.api.attr.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static com.dropchop.recyclone.model.api.attr.AttributeMarshaller.marshall;
import static com.dropchop.recyclone.model.api.attr.AttributeMarshaller.unmarshall;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class JpaAttribute<X> implements Attribute<X> {

  public enum Type {
    str,
    bool,
    num,
    date,
    set,
    list
  }

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private Type type;

  @Column(name="name")
  @EqualsAndHashCode.Include
  private String name;

  @Column(name="value")
  private String strValue;

  @Transient
  private transient X value;

  @Transient
  public X getValue() {
    return this.value;
  }

  @Transient
  public void setValue(X value) {
    this.value = value;
  }


  public static <X> Attribute<X> toAttribute(JpaAttribute<X> jpaAttribute) {
    Attribute<X> attribute;
    if (Type.str == jpaAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(jpaAttribute.getName(), jpaAttribute.getStrValue(), String.class);
    } else if (Type.bool == jpaAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(jpaAttribute.getName(), jpaAttribute.getStrValue(), Boolean.class);
    } else if (Type.date == jpaAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(jpaAttribute.getName(), jpaAttribute.getStrValue(), ZonedDateTime.class);
    } else if (Type.num == jpaAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(jpaAttribute.getName(), jpaAttribute.getStrValue(), BigDecimal.class);
    } else if (Type.set == jpaAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(jpaAttribute.getName(), jpaAttribute.getStrValue(), Set.class);
    } else if (Type.list == jpaAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(jpaAttribute.getName(), jpaAttribute.getStrValue(), List.class);
    } else {
      throw new UnsupportedOperationException("Unsupported JpaAttribute to Attribute conversion");
    }
    attribute.setName(jpaAttribute.getName());

    return attribute;
  }

  public static <X> JpaAttribute<X> fromAttribute(Attribute<X> attribute) {
    if (attribute instanceof AttributeToRemove) {
      return null;
    }
    JpaAttribute<X> jpaAttribute = new JpaAttribute<>();
    jpaAttribute.setName(attribute.getName());
    jpaAttribute.setValue(attribute.getValue());
    jpaAttribute.setStrValue(marshall(attribute));
    if (attribute instanceof AttributeString) {
      jpaAttribute.setType(Type.str);
    } else if (attribute instanceof AttributeBool) {
      jpaAttribute.setType(Type.bool);
    } else if (attribute instanceof AttributeDate) {
      jpaAttribute.setType(Type.date);
    } else if (attribute instanceof AttributeDecimal) {
      jpaAttribute.setType(Type.num);
    } else if (attribute instanceof AttributeSet) {
      jpaAttribute.setType(Type.set);
    } else if (attribute instanceof AttributeValueList<?>) {
      jpaAttribute.setType(Type.list);
    } else {
      throw new UnsupportedOperationException("Unsupported Attribute to JpaAttribute conversion");
    }

    return jpaAttribute;
  }
}
