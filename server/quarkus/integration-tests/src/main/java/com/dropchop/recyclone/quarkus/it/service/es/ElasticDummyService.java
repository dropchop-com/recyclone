package com.dropchop.recyclone.quarkus.it.service.es;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.service.ElasticCrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.entity.es.EsDummy;
import com.dropchop.recyclone.quarkus.it.repo.es.ElasticDummyMapperProvider;
import com.dropchop.recyclone.quarkus.it.repo.es.ElasticDummyRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_ES_DEFAULT;

@Getter
@RequestScoped
@RecycloneType(RECYCLONE_ES_DEFAULT)
@SuppressWarnings({"unused", "CdiInjectionPointsInspection", "RedundantSuppression"})
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

  @Override
  public Result<Dummy> search() {
    QueryParams queryParams = this.executionContext.getParams();
    IQueryObject query = this.repository.provideCondition(queryParams);
    return super.search();
  }
}
