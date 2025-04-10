package com.dropchop.recyclone.base.es.model.query.parser.strategies;

import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;

public class TermClauseStrategy implements QueryClauseStrategy{

  @Override
  public QueryNodeObject buildClause(String field, String value, Boolean caseInsensitive) {
    QueryNodeObject term = new QueryNodeObject();
    QueryNodeObject fieldObject = createFieldValueObject(field, value, caseInsensitive);
    term.put("span_term", fieldObject);
    return term;
  }
}
