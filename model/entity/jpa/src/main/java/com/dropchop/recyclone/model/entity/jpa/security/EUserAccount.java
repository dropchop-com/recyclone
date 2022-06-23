package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.UserAccount;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import lombok.*;

import javax.persistence.Column;
import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class EUserAccount extends EUuid implements UserAccount {

  @Column(name = "title")
  private String title;

  @Column(name = "deactivated")
  private ZonedDateTime deactivated;
}
