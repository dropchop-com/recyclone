package com.dropchop.recyclone.rest.server.security.intern;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.rest.server.ClassicRestByCodeResource;
import com.dropchop.recyclone.service.api.security.DomainService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class DomainResource extends ClassicRestByCodeResource<Domain, CodeParams> implements
    com.dropchop.recyclone.rest.api.internal.security.DomainResource {

  @Inject
  DomainService service;

  @Inject
  CodeParams params;

  @Override
  public Result<Domain> get() {
    return service.search();
  }

  @Override
  public Result<Domain> getByCode(String code) {
    params.setCodes(List.of(code));
    return service.search();
  }

  @Override
  public Result<Domain> search(CodeParams params) {
    return service.search();
  }

  @Override
  public Result<Domain> create(List<Domain> domains) {
    return service.create(domains);
  }

  @Override
  public Result<Domain> delete(List<Domain> domains) {
    return service.delete(domains);
  }

  @Override
  public Result<Domain> update(List<Domain> domains) {
    return service.update(domains);
  }
}
