package com.dropchop.recyclone.base.api.repo.ctx;

import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 03. 22.
 */
public interface QueryExecContextListener extends RepositoryExecContextListener {
  void onQueryPrepared(String query);
}
