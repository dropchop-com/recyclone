package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.base.jpa.repo.security.UserMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.UserRepository;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.api.service.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

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
}
