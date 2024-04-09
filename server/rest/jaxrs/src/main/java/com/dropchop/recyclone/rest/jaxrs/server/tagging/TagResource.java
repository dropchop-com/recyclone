package com.dropchop.recyclone.rest.jaxrs.server.tagging;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.invoke.TagParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.service.api.ExecContextType;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.tagging.TagService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 22.
 */
@Slf4j
@RequestScoped
//@Path(Constants.Paths.PUBLIC_SEGMENT + Constants.Paths.Tagging.TAG)
public class TagResource implements com.dropchop.recyclone.rest.jaxrs.api.tagging.TagResource<Tag> {

  @Inject
  ServiceSelector selector;

  @Inject
  @ExecContextType(com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT)
  ParamsExecContextContainer ctxContainer;

  @Override
  public Result<Tag> get() {
    return selector.select(TagService.class).search();
  }

  @Override
  public Result<Tag> getById(UUID id) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof TagParams tagParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", IdentifierParams.class));
    }
    tagParams.setIdentifiers(List.of(id.toString()));
    return selector.select(TagService.class).search();
  }

  @Override
  public Result<Tag> getByType(String type) {
    Params params = ctxContainer.get().getParams();
    if (!(params instanceof TagParams tagParams)) {
      throw new ServiceException(ErrorCode.parameter_validation_error,
        String.format("Invalid parameter type: should be [%s]", IdentifierParams.class));
    }
    tagParams.setTypes(List.of(type));
    return selector.select(TagService.class).search();
  }

  @Override
  public Result<Tag> search(TagParams parameters) {
    return selector.select(TagService.class).search();
  }
}
