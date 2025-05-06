package com.dropchop.recyclone.base.jaxrs.tagging;

import com.dropchop.recyclone.base.dto.model.invoke.TagParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.base.api.rest.ClassicReadByIdResource;
import com.dropchop.recyclone.base.api.service.tagging.TagService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 06. 22.
 */
@Slf4j
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class TagResource extends ClassicReadByIdResource<Tag, TagParams> implements
    com.dropchop.recyclone.base.api.jaxrs.tagging.TagResource<Tag> {

  @Inject
  TagService service;

  @Inject
  TagParams params;

  @Override
  public Result<Tag> get() {
    return service.search();
  }

  @Override
  public Result<Tag> getById(UUID id) {
    params.setIdentifiers(List.of(id.toString()));
    return service.search();
  }

  @Override
  public Result<Tag> getByType(String type) {
    params.setTypes(List.of(type));
    return service.search();
  }

  @Override
  public List<Tag> getByTypeRest(String type) {
    return unwrap(getByType(type));
  }

  @Override
  public Result<Tag> search(TagParams parameters) {
    return service.search();
  }
}
