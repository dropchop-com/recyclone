package com.dropchop.recyclone.repo.jpa.blaze.security;


import com.dropchop.recyclone.model.entity.jpa.security.EUser;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;

import javax.enterprise.context.ApplicationScoped;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

@ApplicationScoped
@RepositoryType(RCYN_DEFAULT)
public class UserRepository extends BlazeRepository<EUser, UUID> {


  @Override
  public Class<EUser> getRootClass() {
    return EUser.class;
  }
}
