package com.dropchop.recyclone.base.api.mapper;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 03. 22.
 */
public interface TotalCountExecContextListener extends RepositoryExecContextListener {
  void onTotalCount(Long count);
}
