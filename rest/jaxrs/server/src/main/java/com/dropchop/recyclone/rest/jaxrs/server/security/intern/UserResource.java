package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.invoke.UserParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.security.UserService;
import com.dropchop.recyclone.service.api.tagging.TagService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@Path(Paths.INTERNAL_SEGMENT + Paths.Security.USER)
public class UserResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.UserResource {

  @Inject
  ServiceSelector selector;

  @Inject
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  ParamsExecContextContainer ctxContainer;

  @Override
  public Result<User<DtoId>> get() {
    return null;
  }

  @Override
  public Result<User<DtoId>> search(UserParams params) {
    return selector.select(UserService.class).search();
  }

  @Override
  public Result<User<DtoId>> getByUuid(UUID id) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof IdentifierParams identifierParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", IdentifierParams.class));
    }
    identifierParams.setIdentifiers(List.of(id.toString()));
    return selector.select(UserService.class).search();
  }


  @Override
  public Result<User<DtoId>> create(List<User<DtoId>> users) {
    return selector.select(UserService.class).create(users);
  }


  @Override
  public Result<User<DtoId>> update(List<User<DtoId>> users) {
    return selector.select(UserService.class).update(users);
  }
}
