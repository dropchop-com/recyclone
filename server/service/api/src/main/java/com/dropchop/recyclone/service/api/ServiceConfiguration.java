package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.repo.api.CrudServiceRepository;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 2. 05. 22.
 */
public class ServiceConfiguration<D extends Dto, E extends Entity, ID> {

  final CrudServiceRepository<D, E, ID> repository;

  public ServiceConfiguration(CrudServiceRepository<D, E, ID> repository) {
    this.repository = repository;
  }

  public CrudServiceRepository<D, E, ID> getRepository() {
    return repository;
  }
}
