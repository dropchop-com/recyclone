package com.dropchop.recyclone.model.entity.jpa.attr;

import com.dropchop.recyclone.model.api.attr.AttributeBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Data
@NoArgsConstructor
@MappedSuperclass
@Embeddable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class EAttribute<T> extends AttributeBase<T> {

  @Column(name="name")
  private String name;
}
