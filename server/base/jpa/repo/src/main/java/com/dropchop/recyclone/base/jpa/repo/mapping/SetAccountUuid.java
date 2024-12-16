package com.dropchop.recyclone.base.jpa.repo.mapping;

import com.dropchop.recyclone.base.api.mapper.BeforeToEntityListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
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
