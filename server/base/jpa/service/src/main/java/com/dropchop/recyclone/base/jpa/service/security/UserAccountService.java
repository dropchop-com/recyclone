package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.repo.FilteringMapperProvider;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaUserAccount;
import com.dropchop.recyclone.base.jpa.repo.security.UserAccountMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.UserAccountRepository;
import com.dropchop.recyclone.base.jpa.repo.security.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class UserAccountService extends CrudServiceImpl<UserAccount, JpaUserAccount, UUID>
    implements com.dropchop.recyclone.base.api.service.security.UserAccountService {

  @Inject
  UserAccountMapperProvider mapperProvider;

  @Inject
  UserAccountRepository repository;

  @Inject
  UserRepository userRepository;

  @Inject
  CommonExecContext<UserAccount, ?> executionContext;


  @Override
  @Transactional
  public Result<UserAccount> delete(List<UserAccount> dtos) {
    checkDtoPermissions(dtos);
    //get account ids
    List<UUID> acountUuids = dtos.stream().map(UserAccount::getUuid).collect(Collectors.toList());
    //load accounts
    List<JpaUserAccount> loadedAccounts = getRepository().findById(acountUuids);
    //remove the accounts from parent user collection !!!
    loadedAccounts.forEach(loadedAccount -> {
      loadedAccount.getUser().removeAccount(loadedAccount);
    });
    //delete the accounts
    getRepository().delete(loadedAccounts);
    FilteringMapperProvider<UserAccount, JpaUserAccount, ?> mapperProvider = getMapperProvider();
    MappingContext mapContext = mapperProvider.getMappingContextForModify();
    return mapperProvider.getToDtoMapper().toDtosResult(loadedAccounts, mapContext);
  }


}
