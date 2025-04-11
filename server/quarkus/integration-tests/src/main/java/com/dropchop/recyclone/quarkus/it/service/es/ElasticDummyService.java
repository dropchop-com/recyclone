package com.dropchop.recyclone.quarkus.it.service.es;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.service.ElasticCrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.entity.es.EsDummy;
import com.dropchop.recyclone.quarkus.it.repo.es.ElasticDummyMapperProvider;
import com.dropchop.recyclone.quarkus.it.repo.es.ElasticDummyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_ES_DEFAULT;

@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_ES_DEFAULT)
@SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
public class ElasticDummyService extends ElasticCrudServiceImpl<Dummy, EsDummy, String>
  implements com.dropchop.recyclone.quarkus.it.service.api.ElasticDummyService {

  @Inject
  ElasticDummyRepository repository;

  @Inject
  ElasticDummyMapperProvider mapperProvider;

  @Inject
  CommonExecContext<Dummy, ?> executionContext;

  @Override
  public Result<Dummy> query() {
    return null;
  }
}
