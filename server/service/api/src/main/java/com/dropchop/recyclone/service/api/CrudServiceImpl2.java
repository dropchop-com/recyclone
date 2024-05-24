package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.service.api.security.AuthorizationService;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 24. 05. 24.
 */
public class CrudServiceImpl2 <D extends Dto, E extends Entity, ID>
    implements CrudService<D>, EntityByIdService<D, E, ID> {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContextContainer ctxContainer;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  AuthorizationService authorizationService;

  @Override
  public Class<E> getRootClass() {
    return null;
  }

  @Override
  public E findById(ID id) {
    return null;
  }

  @Override
  public List<E> findById(Collection<ID> ids) {
    return List.of();
  }

  @Override
  public List<E> find() {
    return List.of();
  }

  @Override
  public Result<D> search() {
    return null;
  }

  @Override
  public Result<D> create(List<D> dtos) {
    return null;
  }

  @Override
  public Result<D> update(List<D> dtos) {
    return null;
  }

  @Override
  public Result<D> delete(List<D> dtos) {
    return null;
  }

  @Override
  public E findById(D dto) {
    return null;
  }

  @Override
  public String getSecurityDomain() {
    return "";
  }
}
