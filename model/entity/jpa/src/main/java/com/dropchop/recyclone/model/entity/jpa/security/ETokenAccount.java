package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.LoginAccount;
import com.dropchop.recyclone.model.api.security.TokenAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@DiscriminatorValue("TokenAccount")
public class ETokenAccount extends EUserAccount implements TokenAccount {

  @NonNull
  private String token;

}
