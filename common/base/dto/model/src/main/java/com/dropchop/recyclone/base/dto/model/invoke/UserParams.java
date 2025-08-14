package com.dropchop.recyclone.base.dto.model.invoke;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserParams extends TagParams {
  private String loginName;
  private String token;
  private String firstName;
  private String lastName;
  private String email;

  private String searchTerm;
  private Boolean active;

}
