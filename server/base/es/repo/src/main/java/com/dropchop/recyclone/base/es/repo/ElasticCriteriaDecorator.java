package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.invoke.CommonParams;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilterDefaults;
import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class ElasticCriteriaDecorator implements CriteriaDecorator {
  private ElasticExecContext<?> context;

  public void init(ElasticExecContext<?> executionContext) {
    this.context = executionContext;
  }

  protected CommonParams<?, ?, ?, ?> commonParamsGet() {
    Params params = getContext().getParams();
    if (!(params instanceof CommonParams<?, ?, ?, ?> parameters)) {
      log.warn("Wrong parameters instance [{}] should be [{}]", params.getClass(), CommonParams.class);
      return null;
    }
    ResultFilter<?, ?> resultFilter = parameters.getFilter();
    if (resultFilter == null) {
      log.warn("Missing result filter in params [{}]!", params);
      return null;
    }
    ResultFilterDefaults defaults = parameters.getFilterDefaults();
    if (defaults == null) {
      log.warn("Missing result filter defaults in params [{}]!", params);
      return null;
    }
    return parameters;
  }
}