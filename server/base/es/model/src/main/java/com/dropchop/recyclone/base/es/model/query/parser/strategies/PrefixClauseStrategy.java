package com.dropchop.recyclone.base.es.model.query.parser.strategies;

import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;

public class PrefixClauseStrategy implements QueryClauseStrategy {

  @Override
  public QueryNodeObject buildClause(String field, String value, Boolean caseInsensitive) {
    QueryNodeObject multi = new QueryNodeObject();
    QueryNodeObject match = new QueryNodeObject();
    QueryNodeObject prefix = new QueryNodeObject();
    // commented out for now since we don't know if we need it yet
    //if (value != null && caseInsensitive != null && caseInsensitive) {
    //  value = value.trim().toLowerCase();
    //}

    QueryNodeObject fieldObject = createFieldValueObject(field, value, caseInsensitive, true);
    prefix.put("prefix", fieldObject);
    match.put("match", prefix);
    multi.put("span_multi", match);
    return multi;
  }
}
