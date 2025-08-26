package com.dropchop.recyclone.base.api.repo;

import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 5. 8. 25.
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 5. 8. 25.
 **/
public interface ResultConsumer<X> extends RepositoryExecContextListener {
  enum Progress {
    CONTINUE,
    STOP
  }
  Progress accept(X result);
}
