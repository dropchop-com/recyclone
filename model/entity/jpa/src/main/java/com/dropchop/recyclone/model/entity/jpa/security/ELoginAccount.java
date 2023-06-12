package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.LoginAccount;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
@Entity
@DiscriminatorValue("LoginAccount")
public class ELoginAccount extends EUserAccount implements LoginAccount {

  @NonNull
  @Column(name = "login_name")
  private String loginName;

  private String password;
}
