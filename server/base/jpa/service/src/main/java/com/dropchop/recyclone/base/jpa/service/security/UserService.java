package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import com.dropchop.recyclone.base.jpa.repo.security.UserMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

@Getter
@ApplicationScoped
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

  @Override
  public Result<User> delete(List<User> dtos) {
    throw new ServiceException(ErrorCode.process_error, "Not allowed to delete users by default implementation!");
  }
}
