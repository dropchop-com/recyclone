package com.dropchop.recyclone.base.es.model.query.parser.strategies;

import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;

public class WildcardClauseStrategy implements QueryClauseStrategy {

  @Override
  public QueryNodeObject buildClause(String field, String value, Boolean caseInsensitive) {
    QueryNodeObject multi = new QueryNodeObject();
    QueryNodeObject match = new QueryNodeObject();
    QueryNodeObject wildcard = new QueryNodeObject();
    QueryNodeObject fieldObject = createFieldValueObject(field, value, caseInsensitive);
    wildcard.put("wildcard", fieldObject);
    match.put("match", wildcard);
    multi.put("span_multi", match);
    return multi;
  }
}
