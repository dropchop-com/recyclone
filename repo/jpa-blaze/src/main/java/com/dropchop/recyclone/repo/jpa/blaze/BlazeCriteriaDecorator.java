package com.dropchop.recyclone.repo.jpa.blaze;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 03. 22.
 */
public abstract class BlazeCriteriaDecorator<T> implements CriteriaDecorator<T> {

  public static final String DELIM = ".";

  private BlazeExecContext<T> context;

  public void init(BlazeExecContext<T> executionContext) {
    this.context = executionContext;
  }

  public BlazeExecContext<T> getContext() {
    return context;
  }
}
