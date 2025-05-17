package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaUserAccount;
import com.dropchop.recyclone.base.jpa.repo.security.UserAccountMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.UserAccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

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
  CommonExecContext<UserAccount, ?> executionContext;

}
