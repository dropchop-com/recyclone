package com.dropchop.recyclone.base.es.repo.mapper.parser;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;
import com.dropchop.recyclone.base.es.repo.mapper.parser.strategies.PrefixClauseStrategy;
import com.dropchop.recyclone.base.es.repo.mapper.parser.strategies.TermClauseStrategy;
import com.dropchop.recyclone.base.es.repo.mapper.parser.strategies.WildcardClauseStrategy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClauseFactory {
  private final String field;

  public QueryNodeObject createClause(String token) {
    if (token.startsWith("*")) {
      throw new ServiceException(
        ErrorCode.internal_error,
        "AdvancedText search does not support words beginning with [\"*\"]"
      );
    } else if(token.endsWith("*") && token.split("\\*").length == 1) {
      token = token.replace("*", "");
      return new PrefixClauseStrategy().buildClause(field, token);
    } else if(token.contains("*")) {
      return new WildcardClauseStrategy().buildClause(field, token);
    }

    return new TermClauseStrategy().buildClause(field, token);
  }
}
