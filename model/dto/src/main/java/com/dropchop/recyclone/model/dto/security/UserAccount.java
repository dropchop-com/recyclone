package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.base.DtoId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UserAccount extends DtoId implements com.dropchop.recyclone.model.api.security.UserAccount {

  private String title;

  private ZonedDateTime deactivated;

}
