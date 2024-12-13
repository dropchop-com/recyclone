package com.dropchop.recyclone.base.api.model.invoke;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext.Listener;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 03. 22.
 */
public interface CommonExecContext<D extends Dto, ECL extends Listener>
  extends ParamsExecContext<ECL>, DataExecContext<D, ECL>, SecurityExecContext {
}
