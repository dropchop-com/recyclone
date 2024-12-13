package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.base.jpa.repo.security.PermissionMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.PermissionRepository;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.api.service.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class PermissionService extends CrudServiceImpl<Permission, JpaPermission, UUID>
  implements com.dropchop.recyclone.base.api.service.security.PermissionService {

  @Inject
  PermissionRepository repository;

  @Inject
  PermissionMapperProvider mapperProvider;
}
