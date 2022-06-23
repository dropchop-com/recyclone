package com.dropchop.recyclone.model.dto.common;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Person extends DtoId implements com.dropchop.recyclone.model.api.common.Person<Country, Language, TitleTranslation> {
  private Country country;
  private Language language;
  private String firstName;
  private String lastName;
  private String middleName;
  private String maidenName;
  private String title;
  private String initials;
  private Boolean female;
  private String defaultEmail;
}
