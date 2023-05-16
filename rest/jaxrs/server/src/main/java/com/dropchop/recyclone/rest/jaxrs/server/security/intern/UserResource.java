package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.invoke.UserParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.dto.invoke.DefaultExecContext;

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
  @SuppressWarnings("CdiInjectionPointsInspection")
  DefaultExecContext<User<?>> ctx;

  @Override
  public Result<User<?>> get() {
    return null;
  }

  @Override
  public Result<User<?>> search(UserParams params) {
    return null;
  }

  @Override
  public Result<User<?>> getByUuid(UUID id) {
    Params params = ctx.getParams();
    if (!(params instanceof IdentifierParams identifierParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", IdentifierParams.class));
    }
    identifierParams.setIdentifiers(List.of(id.toString()));
    return null;
  }
}
