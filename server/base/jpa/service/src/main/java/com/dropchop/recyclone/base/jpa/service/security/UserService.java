package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.StatusMessage;
import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.rest.ResultStatus;
import com.dropchop.recyclone.base.dto.model.security.LoginAccount;
import com.dropchop.recyclone.base.dto.model.security.TokenAccount;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaLoginAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaTokenAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import com.dropchop.recyclone.base.jpa.model.security.JpaUserAccount;
import com.dropchop.recyclone.base.jpa.repo.security.UserMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.UserRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.*;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

@Getter
@RequestScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class UserService extends CrudServiceImpl<User, JpaUser, UUID>
    implements com.dropchop.recyclone.base.api.service.security.UserService {

  @Inject
  UserRepository repository;

  @Inject
  UserMapperProvider mapperProvider;

  @Inject
  CommonExecContext<User, ?> executionContext;

  @Transactional
  @Override
  public Result<User> deleteAccounts(List<User> users) {
    Result<User> result = new Result<>();
    ResultStatus status = new ResultStatus();
    status.setCode(ResultCode.success);
    StatusMessage message = new StatusMessage();
    status.setMessage(message);
    result.setStatus(status);
    boolean isOk = true;
    if (users == null || users.isEmpty()) {
      message.setText("No users provided!");
      isOk = false;
    }
    if (users != null) {
      for (User user : users) {
        Collection<UserAccount> accounts = user.getAccounts();
        if (accounts == null || accounts.isEmpty()) {
          message.setText("No accounts provided!");
          isOk = false;
          break;
        }
      }
    }
    if (!isOk)  {
      status.setCode(ResultCode.error);
      return result;
    }

    Map<UUID, User> usersMap = new HashMap<>();
    for(User u : users) {
      usersMap.put(UUID.fromString(u.getId()), u);
    }

    List<JpaUser> jpaUsers = this.repository.findById(usersMap.keySet());
    for (JpaUser jpaUser : jpaUsers) {
      User u = usersMap.get(jpaUser.getUuid());
      List<UUID> accountUuids = u.getAccounts().stream().map((UserAccount ua) -> UUID.fromString(ua.getId())).toList();

      Set<JpaUserAccount> currentAccounts = jpaUser.getAccounts();
      List<JpaUserAccount> removeAccounts = new ArrayList<>();
      for (JpaUserAccount account : currentAccounts) {
        if (accountUuids.contains(account.getUuid())) {
          removeAccounts.add(account);
        }
      }
      if (!removeAccounts.isEmpty()) {
        removeAccounts.forEach(currentAccounts::remove);
      }
    }
    this.repository.save(jpaUsers);
    result.setData(users);
    return result;
  }

  @Transactional
  @Override
  public Result<User> updateAccounts(List<User> users) {
    Result<User> result = new Result<>();
    ResultStatus status = new ResultStatus();
    status.setCode(ResultCode.success);
    StatusMessage message = new StatusMessage();
    status.setMessage(message);
    result.setStatus(status);
    boolean isOk = true;
    if (users == null || users.isEmpty()) {
      message.setText("No users provided!");
      isOk = false;
    }
    if (users != null) {
      for (User user : users) {
        Collection<UserAccount> accounts = user.getAccounts();
        if (accounts == null || accounts.isEmpty()) {
          message.setText("No accounts provided!");
          isOk = false;
          break;
        }
      }
    }
    if (!isOk)  {
      status.setCode(ResultCode.error);
      return result;
    }

    Map<UUID, User> usersMap = new HashMap<>();
    Map<UUID, UserAccount> accountsMap = new HashMap<>();
    for(User u : users) {
      usersMap.put(UUID.fromString(u.getId()), u);
      for (UserAccount ua : u.getAccounts()) {
        accountsMap.put(UUID.fromString(ua.getId()), ua);
      }
    }

    List<JpaUser> jpaUsers = this.repository.findById(usersMap.keySet());
    for (JpaUser jpaUser : jpaUsers) {
      User u = usersMap.get(jpaUser.getUuid());
      List<UUID> accountUuids = u.getAccounts().stream().map((UserAccount ua) -> UUID.fromString(ua.getId())).toList();

      for (JpaUserAccount account : jpaUser.getAccounts()) {
        UserAccount ua = accountsMap.get(account.getUuid());
        if (account instanceof JpaTokenAccount jpaTokenAccount) {
          if (ua instanceof TokenAccount tokenAccount) {
            jpaTokenAccount.setToken(tokenAccount.getToken());
            jpaTokenAccount.setModified(ZonedDateTime.now());
            jpaUser.setModified(ZonedDateTime.now());
          }
        }
        if (account instanceof JpaLoginAccount jpaLoginAccount) {
          if (ua instanceof LoginAccount loginAccount) {
            jpaLoginAccount.setPassword(loginAccount.getPassword());
            jpaLoginAccount.setModified(ZonedDateTime.now());
            jpaUser.setModified(ZonedDateTime.now());
          }
        }
      }
    }
    this.repository.save(jpaUsers);
    result.setData(users);
    return result;
  }
}
