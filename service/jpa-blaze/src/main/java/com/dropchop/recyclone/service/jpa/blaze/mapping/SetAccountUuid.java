package com.dropchop.recyclone.service.jpa.blaze.mapping;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.dto.security.UserAccount;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.BeforeToEntityListener;

import java.util.UUID;

/**
 * Sets UUID for UserAccount if not available in dto.
 */
public class SetAccountUuid implements BeforeToEntityListener {

  @Override
  public void before(Model model, Entity entity, MappingContext context) {
    if (model instanceof UserAccount account) {
      if (account.getUuid() == null) {
        account.setUuid(UUID.randomUUID());
      }
    }
  }
}
