package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.base.dto.model.invoke.Params;
import io.quarkus.arc.ClientProxy;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.Unremovable;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 23. 01. 2026.
 */
@Unremovable
@DefaultBean
@Priority(200)
@ApplicationScoped
@SuppressWarnings("unused")
public class ParamsProxyUnwrapper {

  public <X extends Params> X unwrap(X params) {
    boolean isArcProxy = params instanceof ClientProxy;
    if (isArcProxy) {
      return ClientProxy.unwrap(params);
    } else {
      return params;
    }
  }
}
