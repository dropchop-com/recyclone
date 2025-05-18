package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.dto.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@ToString(callSuper = true)
@JsonInclude(NON_NULL)
public class LoginAccount extends UserAccount
  implements com.dropchop.recyclone.base.api.model.security.LoginAccount<User, UserAccount, TitleDescriptionTranslation,
  TitleTranslation, Action, Domain, Permission, Role, Country, Language, Tag> {

  @NonNull
  private String loginName;

  private String password;
}
