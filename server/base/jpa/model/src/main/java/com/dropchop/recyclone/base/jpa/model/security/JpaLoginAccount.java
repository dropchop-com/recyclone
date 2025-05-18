package com.dropchop.recyclone.base.jpa.model.security;

import com.dropchop.recyclone.base.api.model.security.LoginAccount;
import com.dropchop.recyclone.base.jpa.model.localization.JpaCountry;
import com.dropchop.recyclone.base.jpa.model.localization.JpaLanguage;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleTranslation;
import com.dropchop.recyclone.base.jpa.model.tagging.JpaTag;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@Entity
@DiscriminatorValue("LoginAccount")
public class JpaLoginAccount extends JpaUserAccount
  implements LoginAccount<JpaUser, JpaUserAccount, JpaTitleDescriptionTranslation, JpaTitleTranslation,
  JpaAction, JpaDomain, JpaPermission, JpaRole, JpaCountry, JpaLanguage, JpaTag> {

  @NonNull
  @Column(name="login_name")
  private String loginName;

  @Column(name="password")
  private String password;
}
