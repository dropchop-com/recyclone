package com.dropchop.recyclone.repo.jpa.blaze;

import com.dropchop.recyclone.model.api.invoke.CommonParams;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ResultFilter;
import com.dropchop.recyclone.model.api.invoke.ResultFilterDefaults;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 03. 22.
 */
@Slf4j
public abstract class BlazeCriteriaDecorator implements CriteriaDecorator {

  public static final String DELIM = ".";

  private BlazeExecContext<?> context;

  public void init(BlazeExecContext<?> executionContext) {
    this.context = executionContext;
  }

  public BlazeExecContext<?> getContext() {
    return context;
  }

  protected CommonParams<?, ?, ?, ?> commonParamsGet() {
    Params params = getContext().getParams();
    if (!(params instanceof CommonParams parameters)) {
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
