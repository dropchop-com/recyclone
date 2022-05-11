package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.rest.jaxrs.ServiceSelector;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.security.DomainService;
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
@Path(Paths.INTERNAL + Paths.Security.DOMAIN)
@RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Constants.Actions.VIEW)
public class DomainResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.security.DomainResource {

  @Inject
  ServiceSelector selector;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContext<CodeParams, Domain> ctx;

  @Override
  public Result<Domain> get() {
    return selector.select(DomainService.class).search();
  }

  @Override
  public Result<Domain> getByCode(String code) {
    CodeParams params = ctx.getParams();
    params.setCodes(List.of(code));
    return selector.select(DomainService.class).search();
  }

  @Override
  public Result<Domain> search(CodeParams params) {
    return selector.select(DomainService.class).search();
  }

  @Override
  @RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Constants.Actions.CREATE)
  public Result<Domain> create(List<Domain> domains) {
    return selector.select(DomainService.class).create(domains);
  }

  @Override
  @RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Constants.Actions.DELETE)
  public Result<Domain> delete(List<Domain> domains) {
    return selector.select(DomainService.class).delete(domains);
  }

  @Override
  @RequiresPermissions(Domains.Security.DOMAIN + PERM_DELIM + Constants.Actions.UPDATE)
  public Result<Domain> update(List<Domain> domains) {
    return selector.select(DomainService.class).update(domains);
  }
}
