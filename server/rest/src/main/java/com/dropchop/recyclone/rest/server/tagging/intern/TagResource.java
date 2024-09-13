package com.dropchop.recyclone.rest.server.tagging.intern;

import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.rest.server.ClassicModifyResource;
import com.dropchop.recyclone.service.api.tagging.TagService;
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
    com.dropchop.recyclone.rest.api.internal.tagging.TagResource<Tag> {

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
