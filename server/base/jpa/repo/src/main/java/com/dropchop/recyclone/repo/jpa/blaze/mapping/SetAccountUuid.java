package com.dropchop.recyclone.repo.jpa.blaze.mapping;

import com.dropchop.recyclone.mapper.api.BeforeToEntityListener;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;

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
