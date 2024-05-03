package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
public interface ExecContextContainer {

  @SuppressWarnings("unused")
  <D extends Dto> ExecContext<?> get();
}
