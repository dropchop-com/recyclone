package com.dropchop.recyclone.model.entity.jpa.common;

import com.dropchop.recyclone.model.api.common.Person;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class EPerson extends EUuid implements Person<ECountry, ELanguage, ETitleTranslation> {


  @ManyToOne
  @JoinColumn(name = "fk_language_code", referencedColumnName = "code", foreignKey = @ForeignKey(name = "language_code_fk"))
  private ELanguage language;

  @ManyToOne
  @JoinColumn(name = "fk_country_code", referencedColumnName = "code", foreignKey = @ForeignKey(name = "country_code_fk"))
  private ECountry country;

  @Column(name="first_name")
  private String firstName;

  @Column(name="last_name")
  private String lastName;

  @Column(name="middle_name")
  private String middleName;

  @Column(name="maiden_name")
  private String maidenName;

  @Column(name="title")
  private String title;

  @Column(name="initials")
  private String initials;

  @Column(name="is_female")
  private Boolean female;

  @Column(name="default_email")
  private String defaultEmail;

  @Column(name="default_phone")
  private String defaultPhone;

  @Override
  public String toString() {
    return super.toString() + ",first='" + firstName + '\'' +
      ", last='" + lastName + '\'';
  }
}
