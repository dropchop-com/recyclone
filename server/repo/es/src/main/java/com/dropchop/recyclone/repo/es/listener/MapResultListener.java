package com.dropchop.recyclone.repo.es.listener;

import com.dropchop.recyclone.repo.es.mapper.ElasticSearchResult.Hit;

import java.util.Map;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 5. 12. 24
 **/
public interface MapResultListener extends QueryResultListener<Map<String, ?>> {
  void onResult(Hit<Map<String, ?>> hit);
}
