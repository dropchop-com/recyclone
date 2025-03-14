package com.dropchop.recyclone.base.jaxrs.security.internal;

import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.Domain;
import com.dropchop.recyclone.base.api.rest.ClassicRestByCodeResource;
import com.dropchop.recyclone.base.api.service.security.DomainService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class DomainResource extends ClassicRestByCodeResource<Domain, CodeParams> implements
    com.dropchop.recyclone.base.api.jaxrs.internal.security.DomainResource {

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
