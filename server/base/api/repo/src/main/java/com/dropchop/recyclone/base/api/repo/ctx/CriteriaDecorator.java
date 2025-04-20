package com.dropchop.recyclone.base.api.repo.ctx;

import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 03. 22.
 */
public interface CriteriaDecorator<E, R extends RepositoryExecContext<E>> extends RepositoryExecContextListener {

  void init(R ctx);
  void decorate();
}
