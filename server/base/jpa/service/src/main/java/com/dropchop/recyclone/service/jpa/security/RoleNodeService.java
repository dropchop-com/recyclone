package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
import com.dropchop.recyclone.repo.jpa.blaze.security.*;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class RoleNodeService extends CrudServiceImpl<RoleNode, JpaRoleNode, UUID>
  implements com.dropchop.recyclone.service.api.security.RoleNodeService {

  @Inject
  RoleNodeMapperProvider mapperProvider;

  @Inject
  RoleNodeRepository repository;

}
