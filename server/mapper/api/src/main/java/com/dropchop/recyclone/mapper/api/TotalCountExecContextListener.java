package com.dropchop.recyclone.mapper.api;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 03. 22.
 */
public interface TotalCountExecContextListener extends RepositoryExecContextListener {
  void onTotalCount(Long count);
}
