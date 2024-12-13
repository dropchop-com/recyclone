package com.dropchop.recyclone.base.api.mapper;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Model;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 04. 22.
 */
public interface BeforeToDtoListener extends BeforeMappingListener {
  void before(Model model, Dto dto, MappingContext context);
}
