package com.dropchop.recyclone.base.es.repo.mapper.parser.strategies;

import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;

public class WildcardClauseStrategy implements QueryClauseStrategy {
  @Override
  public QueryNodeObject buildClause(String field, String value) {
    QueryNodeObject multi = new QueryNodeObject();
    QueryNodeObject match = new QueryNodeObject();
    QueryNodeObject wildcard = new QueryNodeObject();
    QueryNodeObject fieldObject = new QueryNodeObject();
    QueryNodeObject valueObject = new QueryNodeObject();

    valueObject.put("value", value);
    fieldObject.put(field, valueObject);
    wildcard.put("wildcard", fieldObject);
    match.put("match", wildcard);
    multi.put("span_multi", match);
    return multi;
  }
}
