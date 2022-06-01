package com.dropchop.recyclone.rest.jaxrs.server.security.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.security.DomainService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@Path(Paths.INTERNAL + Paths.Security.DOMAIN)
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
  public Result<Domain> create(List<Domain> domains) {
    return selector.select(DomainService.class).create(domains);
  }

  @Override
  public Result<Domain> delete(List<Domain> domains) {
    return selector.select(DomainService.class).delete(domains);
  }

  @Override
  public Result<Domain> update(List<Domain> domains) {
    return selector.select(DomainService.class).update(domains);
  }
}
