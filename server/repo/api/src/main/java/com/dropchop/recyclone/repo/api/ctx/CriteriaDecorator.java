package com.dropchop.recyclone.repo.api.ctx;

import com.dropchop.recyclone.mapper.api.RepositoryExecContextListener;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 03. 22.
 */
public interface CriteriaDecorator extends RepositoryExecContextListener {

  void decorate();
}
