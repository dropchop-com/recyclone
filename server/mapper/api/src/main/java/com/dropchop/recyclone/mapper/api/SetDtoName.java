package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasName;
import com.dropchop.recyclone.mapper.api.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 05. 22.
 */
public class SetDtoName implements AfterToDtoListener {

  @Override
  public void after(Model model, Dto dto, MappingContext context) {
    if (!(model instanceof HasName hModel)) {
      return;
    }
    if (!(dto instanceof HasName hDto)) {
      return;
    }
    String mName = hModel.getName();
    if (mName == null) {
      return;
    }
    String dName = hDto.getName();
    if (dName != null) {
      return;
    }
    hDto.setName(mName);
  }
}
