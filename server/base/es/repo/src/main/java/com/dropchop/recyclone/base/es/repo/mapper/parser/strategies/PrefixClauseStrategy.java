package com.dropchop.recyclone.base.es.repo.mapper.parser.strategies;

import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;

public class PrefixClauseStrategy implements QueryClauseStrategy{

  @Override
  public QueryNodeObject buildClause(String field, String value) {
    QueryNodeObject multi = new QueryNodeObject();
    QueryNodeObject match = new QueryNodeObject();
    QueryNodeObject prefix = new QueryNodeObject();
    QueryNodeObject fieldObject = new QueryNodeObject();
    QueryNodeObject valueObject = new QueryNodeObject();

    valueObject.put("value", value);
    fieldObject.put(field, valueObject);
    prefix.put("prefix", fieldObject);
    match.put("match", prefix);
    multi.put("span_multi", match);
    return multi;
  }
}
