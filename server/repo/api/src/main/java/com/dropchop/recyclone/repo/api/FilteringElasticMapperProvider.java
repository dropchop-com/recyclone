package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 28. 11. 24
 **/
public abstract class FilteringElasticMapperProvider<D extends Dto, E extends Entity, ID>
  extends FilteringMapperProvider<D, E, ID> {

  public abstract getMapToEntityMapper();
}
