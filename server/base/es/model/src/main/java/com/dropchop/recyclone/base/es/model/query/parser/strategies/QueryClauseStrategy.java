package com.dropchop.recyclone.base.es.model.query.parser.strategies;

import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;

public interface QueryClauseStrategy {
  QueryNodeObject buildClause(String field, String value, Boolean caseInsensitive);

  default QueryNodeObject createFieldValueObject(String field, String value, Boolean caseInsensitive,
                                                 boolean caseInsensitiveInValueObject) {
    QueryNodeObject fieldObject = new QueryNodeObject();
    QueryNodeObject valueObject = new QueryNodeObject();

    valueObject.put("value", value);

    if (caseInsensitive != null) {
      if (caseInsensitiveInValueObject) {
        valueObject.put("case_insensitive", caseInsensitive);
      } else {
        fieldObject.put("case_insensitive", caseInsensitive);
      }
    }
    fieldObject.put(field, valueObject);
    return fieldObject;
  }

  default QueryNodeObject createFieldValueObject(String field, String value, Boolean caseInsensitive) {
    return createFieldValueObject(field, value, caseInsensitive, false);
  }
}
