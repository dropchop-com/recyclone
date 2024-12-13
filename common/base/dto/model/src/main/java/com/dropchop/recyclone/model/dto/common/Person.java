package com.dropchop.recyclone.model.dto.common;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(NON_NULL)
public class Person extends DtoId
  implements com.dropchop.recyclone.base.api.model.common.Person<Country, Language, TitleTranslation> {

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
  private String defaultPhone;
}
