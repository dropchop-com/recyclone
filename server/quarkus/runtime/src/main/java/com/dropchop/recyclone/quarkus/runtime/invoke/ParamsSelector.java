package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.quarkus.runtime.common.Selector;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 05. 24.
 */
public class ParamsSelector implements Selector<Params> {

  @Override
  public <P extends Params> P select(Class<P> rawClass) {
    return Selector.super.select(rawClass);
  }

  @Override
  public <P extends Params> P selectOrThrow(Class<P> rawClass) {
    return Selector.super.selectOrThrow(rawClass);
  }
}
