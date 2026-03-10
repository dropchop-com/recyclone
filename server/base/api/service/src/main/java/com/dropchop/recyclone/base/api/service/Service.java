package com.dropchop.recyclone.base.api.service;

import com.dropchop.recyclone.base.api.mapper.FilteringDtoContext;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.dto.model.invoke.ResultFilter;

import java.util.UUID;

import static com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.ContentFilter.content;
import static com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.result;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 21. 04. 22.
 */
public interface Service {
  String getSecurityDomain();

  default MappingContext getStaticMappingContext(String requestId, ResultFilter filter) {
    com.dropchop.recyclone.base.dto.model.invoke.Params params =
        new com.dropchop.recyclone.base.dto.model.invoke.Params();
    params.setRequestId(requestId == null ? UUID.randomUUID().toString() : requestId);
    params.setFilter(filter);
    FilteringDtoContext mapContext = new FilteringDtoContext();
    mapContext.setParams(params);
    return mapContext;
  }

  default MappingContext getStaticMappingContext(String requestId, int treeLevel) {
    return getStaticMappingContext(requestId, result(content(treeLevel)));
  }
}
