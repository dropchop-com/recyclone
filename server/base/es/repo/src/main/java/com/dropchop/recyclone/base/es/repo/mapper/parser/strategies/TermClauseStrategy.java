package com.dropchop.recyclone.base.es.repo.mapper.parser.strategies;

import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;

public class TermClauseStrategy implements QueryClauseStrategy{

  @Override
  public QueryNodeObject buildClause(String field, String value) {
    QueryNodeObject term = new QueryNodeObject();
    QueryNodeObject fieldObject = new QueryNodeObject();
    QueryNodeObject valueObject = new QueryNodeObject();

    valueObject.put("value", value);
    fieldObject.put(field, valueObject);
    term.put("span_term", fieldObject);
    return term;
  }
}
