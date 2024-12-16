package com.dropchop.recyclone.base.jaxrs.tagging.internal;

import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.rest.server.ClassicModifyResource;
import com.dropchop.recyclone.base.api.service.tagging.TagService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 06. 22.
 */
@Slf4j
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class TagResource extends ClassicModifyResource<Tag> implements
    com.dropchop.recyclone.base.api.jaxrs.internal.tagging.TagResource<Tag> {

  @Inject
  TagService service;

  @Override
  public Result<Tag> create(List<Tag> tags) {
    return service.create(tags);
  }

  @Override
  public Result<Tag> delete(List<Tag> tags) {
    return service.delete(tags);
  }

  @Override
  public Result<Tag> update(List<Tag> tags) {
    return service.update(tags);
  }
}
