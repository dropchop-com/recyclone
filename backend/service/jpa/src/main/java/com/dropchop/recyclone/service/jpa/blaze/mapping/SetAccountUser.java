package com.dropchop.recyclone.service.jpa.blaze.mapping;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.entity.jpa.security.EUser;
import com.dropchop.recyclone.model.entity.jpa.security.EUserAccount;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.AfterToEntityListener;

import java.util.Set;

/**
 * Sets user reference to UserAccount
 */
public class SetAccountUser implements AfterToEntityListener {

  @Override
  public void after(Model model, Entity entity, MappingContext context) {
    if (entity instanceof EUser) {
      Set<EUserAccount> accounts = ((EUser) entity).getAccounts();
      for(EUserAccount account : accounts) {
        account.setUser((EUser) entity);
      }
    }
  }
}
