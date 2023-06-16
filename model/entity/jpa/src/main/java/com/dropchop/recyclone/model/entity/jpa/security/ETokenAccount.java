package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.TokenAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
@Entity
@DiscriminatorValue("TokenAccount")
@Table(indexes = { @Index(name = "idx_user_account_token", columnList = "token")})
public class ETokenAccount extends EUserAccount implements TokenAccount {

  @NonNull
  private String token;
}
