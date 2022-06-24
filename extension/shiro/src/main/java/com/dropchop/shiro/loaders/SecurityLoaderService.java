package com.dropchop.shiro.loaders;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.security.User;

import java.util.Set;
import java.util.UUID;

public interface SecurityLoaderService {

  <O extends DtoId> User<O> loadPrincipalByLoginName(String loginName);
  <O extends DtoId> User<O> loadPrincipalByToken(String token);
  <O extends DtoId> User<O> loadPrincipalById(UUID id);

  <M extends Model> Set<Permission> loadPermissions(Class<M> subject, UUID identifier);

}
