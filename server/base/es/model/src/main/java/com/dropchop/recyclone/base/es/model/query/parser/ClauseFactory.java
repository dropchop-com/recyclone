package com.dropchop.recyclone.base.es.model.query.parser;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.utils.Strings;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import com.dropchop.recyclone.base.es.model.query.parser.strategies.PrefixClauseStrategy;
import com.dropchop.recyclone.base.es.model.query.parser.strategies.TermClauseStrategy;
import com.dropchop.recyclone.base.es.model.query.parser.strategies.WildcardClauseStrategy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClauseFactory {
  private final String field;

  public QueryNodeObject createClause(String token, Boolean caseInsensitive) {
    if (Strings.hasUpperCaseChar(token) && caseInsensitive == null) {
      caseInsensitive = Boolean.FALSE;
    }
    if (token.startsWith("*")) {
      throw new ServiceException(
        ErrorCode.internal_error,
        "AdvancedText search does not support words beginning with [\"*\"]"
      );
    } else if(token.endsWith("*") && token.split("\\*").length == 1) {
      token = token.replace("*", "");
      return new PrefixClauseStrategy().buildClause(field, token, caseInsensitive);
    } else if(token.contains("*")) {
      return new WildcardClauseStrategy().buildClause(field, token, caseInsensitive);
    }

    return new TermClauseStrategy().buildClause(field, token, caseInsensitive);
  }
}
