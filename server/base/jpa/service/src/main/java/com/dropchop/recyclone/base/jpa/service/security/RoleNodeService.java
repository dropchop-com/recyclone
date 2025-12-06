package com.dropchop.recyclone.base.jpa.service.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.security.RoleNode;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNode;
import com.dropchop.recyclone.base.jpa.repo.security.RoleNodeMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.security.RoleNodeRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@RequestScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class RoleNodeService extends CrudServiceImpl<RoleNode, JpaRoleNode, UUID>
  implements com.dropchop.recyclone.base.api.service.security.RoleNodeService {

  @Inject
  RoleNodeMapperProvider mapperProvider;

  @Inject
  RoleNodeRepository repository;

  @Inject
  CommonExecContext<RoleNode, ?> executionContext;
}
