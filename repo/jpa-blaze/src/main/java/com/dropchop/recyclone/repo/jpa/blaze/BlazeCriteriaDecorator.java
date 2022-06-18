package com.dropchop.recyclone.repo.jpa.blaze;

import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 03. 22.
 */
public abstract class BlazeCriteriaDecorator implements CriteriaDecorator {

  public static final String DELIM = ".";

  private BlazeExecContext<?> context;

  public void init(BlazeExecContext<?> executionContext) {
    this.context = executionContext;
  }

  public BlazeExecContext<?> getContext() {
    return context;
  }
}
