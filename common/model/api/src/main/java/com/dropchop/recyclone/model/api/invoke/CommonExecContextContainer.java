package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 22.
 */
public interface CommonExecContextContainer extends ParamsExecContextContainer {

  @SuppressWarnings("unused")
  <D extends Dto> CommonExecContext<D, ?> get();
}
