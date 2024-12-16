package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.repo.api.FilteringMapperProvider;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 28. 11. 24
 **/
public abstract class FilteringElasticMapperProvider<D extends Dto, E extends Entity, ID>
  extends FilteringMapperProvider<D, E, ID> implements ElasticMapperProvider {

}
