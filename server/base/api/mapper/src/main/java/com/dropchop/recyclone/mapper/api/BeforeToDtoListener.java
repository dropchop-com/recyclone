package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.mapper.api.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 04. 22.
 */
public interface BeforeToDtoListener extends BeforeMappingListener {
  void before(Model model, Dto dto, MappingContext context);
}
