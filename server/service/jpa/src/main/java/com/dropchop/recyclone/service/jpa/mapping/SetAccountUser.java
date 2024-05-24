package com.dropchop.recyclone.service.jpa.mapping;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUserAccount;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.api.AfterToEntityListener;

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
