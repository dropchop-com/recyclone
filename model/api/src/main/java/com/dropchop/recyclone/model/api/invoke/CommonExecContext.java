package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.ExecContext.Listener;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 03. 22.
 */
public interface CommonExecContext<D extends Dto, ECL extends Listener>
  extends ParamsExecContext<ECL>, DataExecContext<D, ECL>, SecurityExecContext {
}
