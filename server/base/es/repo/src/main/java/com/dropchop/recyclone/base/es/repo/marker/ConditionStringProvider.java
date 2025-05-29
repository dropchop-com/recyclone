package com.dropchop.recyclone.base.es.repo.marker;

import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;

public interface ConditionStringProvider {
  String provideConditionString(QueryParams params);
}
