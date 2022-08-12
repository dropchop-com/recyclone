package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.dto.invoke.CommonExecContext;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.core.UriInfo;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
public interface ExecContextProvider {

  <D extends Dto> CommonExecContext<D> create(UriInfo uriInfo);

  @Produces
  @RequestScoped
  <P extends Params, D extends Dto> CommonExecContext<D> get();

  <P extends Params> void setParams(P p);
}
