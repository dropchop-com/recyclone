package com.dropchop.recyclone.service.jpa.blaze;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.entity.jpa.security.EAction;
import com.dropchop.recyclone.model.entity.jpa.security.EDomain;
import com.dropchop.recyclone.model.entity.jpa.security.EPermission;
import com.dropchop.recyclone.service.api.mapping.AfterToEntityListener;
import com.dropchop.recyclone.service.api.mapping.MappingContext;
import com.dropchop.recyclone.service.jpa.blaze.security.ActionService;
import com.dropchop.recyclone.service.jpa.blaze.security.DomainService;

import java.util.Optional;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 25. 05. 22.
 */
public class PermissionToEntityListener<P extends Params>
  implements AfterToEntityListener<P> {

  private final DomainService domainService;

  private final ActionService actionService;

  public PermissionToEntityListener(DomainService domainService, ActionService actionService) {
    this.domainService = domainService;
    this.actionService = actionService;
  }

  @Override
  public void after(Dto dto, Entity entity, MappingContext<P> context) {
    if (entity instanceof EPermission) {
      EAction action = ((EPermission) entity).getAction();
      if (action == null) {
        throw new ServiceException(ErrorCode.data_validation_error, "Missing permission action!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
      String actionCode = action.getCode();
      if (actionCode == null || actionCode.isBlank()) {
        throw new ServiceException(ErrorCode.data_validation_error, "Missing permission action code!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
      Optional<EAction> optAction = actionService.findById(actionCode);
      if (optAction.isEmpty()) {
        throw new ServiceException(ErrorCode.data_validation_error, "Unable to find permission action for code!",
          Set.of(
            new AttributeString(action.identifierField(), action.identifier()),
            new AttributeString(dto.identifierField(), dto.identifier()))
          );
      }
      ((EPermission) entity).setAction(optAction.get());

      EDomain domain = ((EPermission) entity).getDomain();
      if (domain == null) {
        throw new ServiceException(ErrorCode.data_validation_error, "Missing permission domain!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
      String domainCode = domain.getCode();
      if (domainCode == null || domainCode.isBlank()) {
        throw new ServiceException(ErrorCode.data_validation_error, "Missing permission domain code!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
      Optional<EDomain> optDomain = domainService.findById(domainCode);
      if (optDomain.isEmpty()) {
        throw new ServiceException(ErrorCode.data_validation_error, "Unable to find permission domain for code!",
          Set.of(
            new AttributeString(domain.identifierField(), domain.identifier()),
            new AttributeString(dto.identifierField(), dto.identifier()))
        );
      }
      ((EPermission) entity).setDomain(optDomain.get());
    }
  }
}
