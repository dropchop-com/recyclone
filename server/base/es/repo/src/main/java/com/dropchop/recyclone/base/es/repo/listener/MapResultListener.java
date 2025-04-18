package com.dropchop.recyclone.base.es.repo.listener;

import java.util.Map;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 5. 12. 24
 **/
public interface MapResultListener extends QueryResultListener<Map<String, ?>> {
  Progress onResult(Map<String, ?> result);
}
