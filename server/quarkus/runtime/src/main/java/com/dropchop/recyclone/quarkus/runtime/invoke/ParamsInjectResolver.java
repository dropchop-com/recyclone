package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.model.api.invoke.Params;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 04. 24.
 */
@ApplicationScoped
public class ParamsInjectResolver {

  public <P extends Params> P createParams(Class<P> paramsClass) {
    try {
      return paramsClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
