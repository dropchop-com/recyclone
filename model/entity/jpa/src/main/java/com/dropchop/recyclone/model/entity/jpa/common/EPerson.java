package com.dropchop.recyclone.model.entity.jpa.common;

import com.dropchop.recyclone.model.api.common.Person;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class EPerson extends EUuid implements Person {

  @Column(name="first_name")
  private String firstName;

  @Column(name="last_name")
  private String lastName;

  @Override
  public String toString() {
    return super.toString() + "first='" + firstName + '\'' +
      ", last='" + lastName + '\'';
  }
}
