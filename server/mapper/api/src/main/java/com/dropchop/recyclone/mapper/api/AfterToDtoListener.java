package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.mapper.api.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 04. 22.
 */
public interface AfterToDtoListener extends AfterMappingListener {
  void after(Model model, Dto dto, MappingContext context);
}
