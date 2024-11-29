package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;

import java.util.Map;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 28. 11. 24
 **/
public abstract class FilteringElasticMapperProvider<D extends Dto, E extends Entity, ID>
  extends FilteringMapperProvider<D, E, ID> implements ElasticMapperProvider {

}
