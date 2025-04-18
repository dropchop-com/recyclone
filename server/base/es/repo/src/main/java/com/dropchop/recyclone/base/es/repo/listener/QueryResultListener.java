package com.dropchop.recyclone.base.es.repo.listener;

import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 5. 12. 24
 **/
public interface QueryResultListener<X> extends RepositoryExecContextListener {
  enum Progress {
    CONTINUE,
    STOP
  }
  Progress onResult(X result);
}
