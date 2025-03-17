package com.dropchop.recyclone.base.es.repo.mapper.parser.strategies;

import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;

public interface QueryClauseStrategy {
  QueryNodeObject buildClause(String field, String value);
}
