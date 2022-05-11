package com.dropchop.recyclone.model.entity.jpa.attr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 05. 22.
 */
@Data
@NoArgsConstructor
@MappedSuperclass
@Embeddable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class EAttributeString extends EAttribute<String> {

  @Column(name="value")
  private String value;
}
