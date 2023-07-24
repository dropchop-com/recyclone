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
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class EAttribute<X> implements Attribute<X> {

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


  public static <X> Attribute<X> toAttribute(EAttribute<X> eAttribute) {
    Attribute<X> attribute;
    if (Type.str == eAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(eAttribute.getName(), eAttribute.getStrValue(), String.class);
    } else if (Type.bool == eAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(eAttribute.getName(), eAttribute.getStrValue(), Boolean.class);
    } else if (Type.date == eAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(eAttribute.getName(), eAttribute.getStrValue(), ZonedDateTime.class);
    } else if (Type.num == eAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(eAttribute.getName(), eAttribute.getStrValue(), BigDecimal.class);
    } else if (Type.set == eAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(eAttribute.getName(), eAttribute.getStrValue(), Set.class);
    } else if (Type.list == eAttribute.type) {
      //noinspection unchecked
      attribute = (Attribute<X>) unmarshall(eAttribute.getName(), eAttribute.getStrValue(), List.class);
    } else {
      throw new UnsupportedOperationException("Unsupported EAttribute to Attribute conversion");
    }
    attribute.setName(eAttribute.getName());

    return attribute;
  }

  public static <X> EAttribute<X> fromAttribute(Attribute<X> attribute) {
    if (attribute instanceof AttributeToRemove) {
      return null;
    }
    EAttribute<X> eAttribute = new EAttribute<>();
    eAttribute.setName(attribute.getName());
    eAttribute.setValue(attribute.getValue());
    eAttribute.setStrValue(marshall(attribute));
    if (attribute instanceof AttributeString) {
      eAttribute.setType(Type.str);
    } else if (attribute instanceof AttributeBool) {
      eAttribute.setType(Type.bool);
    } else if (attribute instanceof AttributeDate) {
      eAttribute.setType(Type.date);
    } else if (attribute instanceof AttributeDecimal) {
      eAttribute.setType(Type.num);
    } else if (attribute instanceof AttributeSet) {
      eAttribute.setType(Type.set);
    } else if (attribute instanceof AttributeValueList<?>) {
      eAttribute.setType(Type.list);
    } else {
      throw new UnsupportedOperationException("Unsupported Attribute to EAttribute conversion");
    }

    return eAttribute;
  }
}
