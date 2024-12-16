package com.dropchop.recyclone.base.jpa.model.security;

import com.dropchop.recyclone.base.api.model.security.TokenAccount;
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
@DiscriminatorValue("TokenAccount")
public class JpaTokenAccount extends JpaUserAccount implements TokenAccount {

  @NonNull
  private String token;
}
