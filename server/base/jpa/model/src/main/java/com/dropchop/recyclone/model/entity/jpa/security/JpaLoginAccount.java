package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.base.api.model.security.LoginAccount;
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
public class JpaLoginAccount extends JpaUserAccount implements LoginAccount {

  @NonNull
  @Column(name="login_name")
  private String loginName;

  private String password;
}
