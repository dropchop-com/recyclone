package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.repo.FilteringMapperProvider;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 28. 11. 24
 **/
public abstract class FilteringElasticMapperProvider<D extends Dto, E extends Entity, ID>
  extends FilteringMapperProvider<D, E, ID> implements ElasticMapperProvider {
}
