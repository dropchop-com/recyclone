package com.dropchop.recyclone.rest.jaxrs.server.tagging.intern;

import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.tagging.TagService;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 22.
 */
@Slf4j
@RequestScoped
@Path(Constants.Paths.INTERNAL_SEGMENT + Constants.Paths.Tagging.TAG)
public class TagResource
  implements com.dropchop.recyclone.rest.jaxrs.api.intern.tagging.TagResource<Tag> {

  @Inject
  ServiceSelector selector;

  @Override
  public Result<Tag> create(List<Tag> tags) {
    return selector.select(TagService.class).create(tags);
  }

  @Override
  public Result<Tag> delete(List<Tag> tags) {
    return selector.select(TagService.class).delete(tags);
  }

  @Override
  public Result<Tag> update(List<Tag> tags) {
    return selector.select(TagService.class).update(tags);
  }
}
