package com.dropchop.recyclone.rest.jaxrs.server;

import com.dropchop.recyclone.service.api.CommonExecContextConsumer;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 02. 22.
 */
@Slf4j
public class ExecContextCreateFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    log.debug("Created thread local {}", CommonExecContextConsumer.provider.create());
  }
}
