package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.RoleNodePermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermission;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodePermissionMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.RoleNodePermissionRepository;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class RoleNodePermissionService extends CrudServiceImpl<RoleNodePermission, JpaRoleNodePermission, UUID>
  implements com.dropchop.recyclone.service.api.security.RoleNodePermissionService {

  @Inject
  RoleNodePermissionMapperProvider mapperProvider;

  @Inject
  RoleNodePermissionRepository repository;


  @Override
  protected Result<RoleNodePermission> createOrUpdate(List<RoleNodePermission> dtos) {
    return super.createOrUpdate(dtos);
  }
}
