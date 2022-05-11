package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.Dto;
import com.dropchop.recyclone.model.api.invoke.Params;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 01. 22.
 */
public interface CommonExecContextConsumer<P extends Params, D extends Dto> {

  CommonExecContextProvider provider = new CommonExecContextProvider();

  default CommonExecContext<P, D> context() {
    return provider.get();
  }

  default P params() {
    Params p = provider.getParams();
    //noinspection unchecked
    return (P)p;
  }
}
