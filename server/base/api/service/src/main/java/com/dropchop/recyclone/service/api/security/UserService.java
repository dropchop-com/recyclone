package com.dropchop.recyclone.service.api.security;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.service.api.CrudService;

public interface UserService extends CrudService<User> {

  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Security.USER;
  }

}
