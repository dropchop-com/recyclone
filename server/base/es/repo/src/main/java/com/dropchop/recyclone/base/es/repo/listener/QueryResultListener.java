package com.dropchop.recyclone.base.es.repo.listener;

import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.es.repo.mapper.ElasticSearchResult.Hit;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 5. 12. 24
 **/
public interface QueryResultListener<X> extends RepositoryExecContextListener {
  void onResult(Hit<X> result);
}
