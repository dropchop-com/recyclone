package com.dropchop.recyclone.repo.api.ctx;

import com.dropchop.recyclone.model.api.invoke.ExecContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 03. 22.
 */
public interface QueryExecContextListener extends ExecContext.Listener {
  void onQueryPrepared(String query);
}
