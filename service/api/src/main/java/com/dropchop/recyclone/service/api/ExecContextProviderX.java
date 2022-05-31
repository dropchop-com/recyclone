package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Params;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 31. 05. 22.
 */
public interface ExecContextProviderX {
  <P extends Params, D extends Dto> CommonExecContext<P, D> create();

  @Produces
  @RequestScoped
  <P extends Params, D extends Dto> CommonExecContext<P, D> get();

  <P extends Params> void setParams(P p);
}
