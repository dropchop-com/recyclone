package com.dropchop.recyclone.repo.jpa.blaze;

import com.dropchop.recyclone.base.api.model.invoke.CommonParams;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilterDefaults;
import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 03. 22.
 */
@Slf4j
@Getter
public abstract class BlazeCriteriaDecorator implements CriteriaDecorator {

  public static final String DELIM = ".";

  private BlazeExecContext<?> context;

  public void init(BlazeExecContext<?> executionContext) {
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
