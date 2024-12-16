package com.dropchop.recyclone.base.api.model.invoke;

import com.dropchop.recyclone.base.api.model.base.Dto;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 22.
 */
public interface ParamsExecContextContainer extends ExecContextContainer {

  @SuppressWarnings("unused")
  <D extends Dto> ParamsExecContext<?> get();
}
