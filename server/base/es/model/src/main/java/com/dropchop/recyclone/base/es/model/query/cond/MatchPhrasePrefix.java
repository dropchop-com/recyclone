package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.QueryField;
import com.dropchop.recyclone.base.es.model.query.QueryObject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
@Getter
public class MatchPhrasePrefix extends QueryField {

  private final String analyzer;
  private final Integer slop;
  private final Integer maxExpansions;

  public MatchPhrasePrefix(IQueryNode parent, String fieldName, String value, Integer slop,
                           String analyzer, Integer maxExpansions) {
    super(parent, fieldName, value);
    this.analyzer = analyzer;
    this.slop = slop;
    this.maxExpansions = maxExpansions;
    QueryObject paramsObject = new QueryObject();
    QueryObject valueObject = new QueryObject();

    valueObject.put("query", value);
    paramsObject.putAll(valueObject);

    if(analyzer != null) {
      QueryObject anaObject = new QueryObject();
      anaObject.put("analyzer", analyzer);
      paramsObject.putAll(anaObject);
    }

    if (slop != null) {
      QueryObject caseObject = new QueryObject();
      caseObject.put("slop", slop);
      paramsObject.putAll(caseObject);
    }

    if (maxExpansions != null) {
      QueryObject caseObject = new QueryObject();
      caseObject.put("max_expansions", maxExpansions);
      paramsObject.putAll(caseObject);
    }

    body.put(fieldName, paramsObject);
    this.put("match_phrase_prefix", body);
  }
}
