package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.TokenAccount;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
@Entity
@DiscriminatorValue("TokenAccount")
public class ETokenAccount extends EUserAccount implements TokenAccount {

  @NonNull
  private String token;
}
