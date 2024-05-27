package com.dropchop.recyclone.repo.api.ctx;

import com.dropchop.recyclone.mapper.api.RepositoryExecContextListener;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 03. 22.
 */
public interface QueryExecContextListener extends RepositoryExecContextListener {
  void onQueryPrepared(String query);
}
