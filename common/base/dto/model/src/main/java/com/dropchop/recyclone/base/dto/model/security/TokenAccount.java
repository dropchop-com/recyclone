package com.dropchop.recyclone.base.dto.model.security;

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
public class TokenAccount extends UserAccount
  implements com.dropchop.recyclone.base.api.model.security.TokenAccount {

  @NonNull
  private String token;
}
