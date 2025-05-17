package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.api.service.CrudService;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;

public interface UserAccountService extends CrudService<UserAccount> {

  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Security.USER;
  }

}
