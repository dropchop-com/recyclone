package com.dropchop.recyclone.model.dto.security;

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
  implements com.dropchop.recyclone.model.api.security.LoginAccount {

  @NonNull
  private String loginName;

  @JsonIgnore
  private String password;
}
