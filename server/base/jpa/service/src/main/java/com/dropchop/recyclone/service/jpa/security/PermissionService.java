package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.repo.jpa.blaze.security.PermissionMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.security.PermissionRepository;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
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
  implements com.dropchop.recyclone.service.api.security.PermissionService {

  @Inject
  PermissionRepository repository;

  @Inject
  PermissionMapperProvider mapperProvider;
}
