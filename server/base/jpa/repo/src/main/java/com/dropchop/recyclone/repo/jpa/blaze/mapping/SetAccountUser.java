package com.dropchop.recyclone.repo.jpa.blaze.mapping;

import com.dropchop.recyclone.mapper.api.AfterToEntityListener;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUserAccount;

import java.util.Set;

/**
 * Sets user reference to UserAccount
 */
public class SetAccountUser implements AfterToEntityListener {

  @Override
  public void after(Model model, Entity entity, MappingContext context) {
    if (entity instanceof JpaUser) {
      Set<JpaUserAccount> accounts = ((JpaUser) entity).getAccounts();
      for(JpaUserAccount account : accounts) {
        account.setUser((JpaUser) entity);
      }
    }
  }
}
