package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.security.EAction;
import com.dropchop.recyclone.model.entity.jpa.security.EDomain;
import com.dropchop.recyclone.model.entity.jpa.security.EPermission;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.PermissionRepository;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.api.mapping.MappingContext;
import com.dropchop.recyclone.service.jpa.blaze.CrudServiceImpl;
import com.dropchop.recyclone.service.jpa.blaze.RelatedAllPreloadedEntityLoader;
import com.dropchop.recyclone.service.jpa.blaze.RelatedEntityLoader;
import com.dropchop.recyclone.service.jpa.blaze.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RCYN_DEFAULT)
public class PermissionService extends CrudServiceImpl<Permission, CodeParams, EPermission, UUID>
  implements com.dropchop.recyclone.service.api.security.PermissionService {

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  PermissionRepository repository;

  @Inject
  PermissionToDtoMapper toDtoMapper;

  @Inject
  PermissionToEntityMapper toEntityMapper;

  @Inject
  @ServiceType(RCYN_DEFAULT)
  ActionService actionService;

  @Inject
  @ServiceType(RCYN_DEFAULT)
  DomainService domainService;

  @Override
  public ServiceConfiguration<Permission, CodeParams, EPermission, UUID> getConfiguration(CommonExecContext<CodeParams, Permission> execContext) {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper,
      execContext
    );
  }

  @Override
  protected MappingContext<CodeParams> constructToEntityMappingContext() {
    MappingContext<CodeParams> context = super.constructToEntityMappingContext();
    context.listeners()
      .addAll(
        List.of(
          new RelatedAllPreloadedEntityLoader<Action, EAction, String, CodeParams>(actionService)
            .forActionOnly(Actions.CREATE)
            .forActionOnly(Actions.UPDATE),
          new RelatedEntityLoader<Domain, EDomain, String, CodeParams>(domainService)
            .forActionOnly(Actions.CREATE)
            .forActionOnly(Actions.UPDATE)
        )
      );
    return context;
  }
}
