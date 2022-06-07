package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.invoke.UserParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
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
  CommonExecContext<UserParams, User<?>> ctx;

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
    UserParams params = ctx.getParams();
    params.setIdentifiers(List.of(id.toString()));
    return null;
  }
}
