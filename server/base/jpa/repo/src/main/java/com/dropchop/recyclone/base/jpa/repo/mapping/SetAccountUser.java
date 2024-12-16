package com.dropchop.recyclone.base.jpa.repo.mapping;

import com.dropchop.recyclone.base.api.mapper.AfterToEntityListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import com.dropchop.recyclone.base.jpa.model.security.JpaUserAccount;

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
