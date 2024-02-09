package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.security.EUser;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.UserRepository;
import com.dropchop.recyclone.service.api.ServiceConfiguration;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.jpa.blaze.RecycloneCrudServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

@Slf4j
@ApplicationScoped
@ServiceType(RCYN_DEFAULT)
public class UserService extends RecycloneCrudServiceImpl<User<DtoId>, EUser<EUuid>, UUID>  implements com.dropchop.recyclone.service.api.security.UserService {


  @Inject
  @RepositoryType(RCYN_DEFAULT)
  UserRepository userRepository;

  @Inject
  UserToDtoMapper toDtoMapper;

  @Inject
  UserToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<User<DtoId>, EUser<EUuid>, UUID> getConfiguration() {
    return new ServiceConfiguration<>(
      userRepository,
      toDtoMapper,
      toEntityMapper
    );
  }


}
