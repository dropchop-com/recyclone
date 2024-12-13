package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.repo.jpa.blaze.security.UserMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.UserRepository;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
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
    implements com.dropchop.recyclone.service.api.security.UserService {

  @Inject
  UserRepository repository;

  @Inject
  UserMapperProvider mapperProvider;
}
