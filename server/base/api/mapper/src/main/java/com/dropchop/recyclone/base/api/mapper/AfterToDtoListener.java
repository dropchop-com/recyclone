package com.dropchop.recyclone.base.api.mapper;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Model;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 04. 22.
 */
public interface AfterToDtoListener extends AfterMappingListener {
  void after(Model model, Dto dto, MappingContext context);
}
