package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.ExecContext;

import javax.ws.rs.core.UriInfo;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
public interface ExecContextProvider {

  Class<? extends ExecContext> getContextClass();

  <D extends Dto> ExecContext<?> create(UriInfo uriInfo);

  <D extends Dto> ExecContext<?> produce();
}
