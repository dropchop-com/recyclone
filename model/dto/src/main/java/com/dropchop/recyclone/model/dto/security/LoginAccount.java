package com.dropchop.recyclone.model.dto.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
@ToString(callSuper = true)
@JsonInclude(NON_NULL)
public class LoginAccount extends UserAccount
  implements com.dropchop.recyclone.model.api.security.LoginAccount {

  @NonNull
  private String loginName;

  @JsonIgnore
  private String password;
}
