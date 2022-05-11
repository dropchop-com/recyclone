package com.dropchop.recyclone.model.dto.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class LoginAccount extends UserAccount implements com.dropchop.recyclone.model.api.security.LoginAccount {
  @NonNull
  private String loginName;

  @JsonIgnore
  private String password;
}
