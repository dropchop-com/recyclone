package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.security.ActionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@Path(Paths.INTERNAL + Paths.Security.ACTION)
@RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.VIEW)
public class ActionResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.ActionResource {

  @Inject
  ServiceSelector selector;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContext<CodeParams, Action> ctx;

  @Override
  public Result<Action> getByCode(String code) {
    CodeParams params = ctx.getParams();
    params.setCodes(List.of(code));
    return selector.select(ActionService.class).search();
  }

  @Override
  public Result<Action> get() {
    return selector.select(ActionService.class).search();
  }

  @Override
  public Result<Action> search(CodeParams params) {
    return selector.select(ActionService.class).search();
  }

  @Override
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.CREATE)
  public Result<Action> create(List<Action> actions) {
    return selector.select(ActionService.class).create(actions);
  }

  @Override
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.DELETE)
  public Result<Action> delete(List<Action> actions) {
    return selector.select(ActionService.class).delete(actions);
  }

  @Override
  @RequiresPermissions(Domains.Security.ACTION + PERM_DELIM + Actions.UPDATE)
  public Result<Action> update(List<Action> actions) {
    return selector.select(ActionService.class).update(actions);
  }
}
