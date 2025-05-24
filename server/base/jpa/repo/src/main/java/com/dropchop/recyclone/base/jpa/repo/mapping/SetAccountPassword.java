package com.dropchop.recyclone.base.jpa.repo.mapping;

import com.dropchop.recyclone.base.api.mapper.AfterToEntityListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.repo.ReadRepository;
import com.dropchop.recyclone.base.api.repo.mapper.EntityLoadDelegate;
import com.dropchop.recyclone.base.dto.model.security.LoginAccount;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaLoginAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import com.dropchop.recyclone.base.jpa.model.security.JpaUserAccount;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * Sets user reference to UserAccount
 */
@Slf4j
public class SetAccountPassword extends EntityLoadDelegate<UserAccount, JpaUserAccount, UUID>  implements AfterToEntityListener {

  public SetAccountPassword(ReadRepository<JpaUserAccount, UUID> repository) {
    super(repository);
  }

  public SetAccountPassword(ReadRepository<JpaUserAccount, UUID> repository, Collection<Class<JpaUserAccount>> supported) {
    super(repository, supported);
  }


  private String getPasswordHash(String password) {
    try {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] digest = md.digest(password.getBytes());
    StringBuilder sb = new StringBuilder();
    for (byte b : digest) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      log.error("Cannot get MD5 hash ", e);
      return null;
    }
  }

  @Override
  public void after(Model model, Entity entity, MappingContext context) {
    if (entity instanceof JpaLoginAccount loginAccount) {
      JpaUserAccount loadedAccount = this.findById((UserAccount) model);
      String password = this.getPasswordHash(loginAccount.getPassword());
      if (password == null || password.isBlank()) {
        ((JpaLoginAccount) entity).setPassword(null);
      } else {
        if (loadedAccount != null && loadedAccount instanceof JpaLoginAccount acc) {
          if (!acc.getPassword().equals(password)) {
            loginAccount.setPassword(password);
            loginAccount.setModified(ZonedDateTime.now());
          }
        } else {
          loginAccount.setPassword(password);
          loginAccount.setModified(ZonedDateTime.now());
        }
      }
    }
  }
}
