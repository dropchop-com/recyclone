package com.dropchop.recyclone.repo.api.ctx;

import com.dropchop.recyclone.model.api.invoke.ExecContext.Listener;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 22.
 */
public interface RepositoryExecContext<E, P extends Params> extends ParamsExecContext<P, Listener> {

  Iterable<? extends CriteriaDecorator<E>> getCriteriaDecorators();
}
