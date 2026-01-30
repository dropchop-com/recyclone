package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.api.model.legacy.text.ExpressionParser;
import com.dropchop.recyclone.base.api.model.legacy.text.ExpressionToken;
import com.dropchop.recyclone.base.api.model.legacy.text.Or;
import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.es.model.query.QueryField;
import lombok.Getter;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
@Getter
public class MatchText extends QueryField {
  private final Boolean caseInsensitive;
  private final List<ExpressionToken> tokens;
  private final Integer slop;

  private void mapAdvancedText(Bool query, String fieldName, AdvancedText advancedText, List<ExpressionToken> tokens) {
    for(ExpressionToken token : tokens) {
      IQueryObject subQuery = null;
      if (token instanceof com.dropchop.recyclone.base.api.model.legacy.text.Phrase phrase) {
        List<ExpressionToken> subTokens = phrase.getExpressionTokens();
        if (subTokens.size() == 1) {
          StringBuilder sequence = subTokens.getFirst().getExpression();
          if (sequence.length() <= 1) {
            continue;
          }
          //subQuery = new QueryNodeObject.MatchNodeObject(name, sequence.toString());
        } else if (subTokens.size() > 1) {
          SpanNear spanQuery = new SpanNear(query, advancedText.getSlop(), advancedText.getInOrder());
          boolean added = false;
          for (ExpressionToken subToken : subTokens) {
            if (subToken.isWildcard()) {
              StringBuilder sequence = subToken.getExpression();
              spanQuery.addClause(new SpanMulti(query, new com.dropchop.recyclone.base.es.model.query.cond.Wildcard(
                  query, fieldName, sequence.toString().toLowerCase(), advancedText.getCaseInsensitive()
              )));
              added = true;
            } else if (subToken.isPrefix()) {
              StringBuilder sequence = subToken.getExpression();
              String tokenVal = sequence.substring(0, sequence.length() - 1);
              if (tokenVal.isEmpty()) {
                continue;
              }
              spanQuery.addClause(new SpanMulti(
                  query,
                  new Prefix(query, fieldName, tokenVal.toLowerCase(), advancedText.getCaseInsensitive())
              ));
              added = true;
            } else {
              StringBuilder sequence = subToken.getExpression();
              if (sequence.length() <= 0) {
                continue;
              }
              spanQuery.addClause(new SpanTerm(query, fieldName, sequence.toString().toLowerCase()));
              added = true;
            }
          }
          if (!added) {
            continue;
          }
          subQuery = spanQuery;
        }
      } else if (token instanceof Or orTokens) {
        this.mapAdvancedText(query, fieldName, advancedText, orTokens.getExpressionTokens());
      } else if (token.isWildcard()) {
        StringBuilder sequence = token.getExpression();
        String tok = sequence.toString().toLowerCase();
        subQuery = new com.dropchop.recyclone.base.es.model.query.cond.Wildcard(
            query, fieldName, tok, advancedText.getCaseInsensitive()
        );
      } else if (token.isPrefix()) {
        StringBuilder sequence = token.getExpression();
        if (sequence.length() <= 1) {
          continue;
        }
        String tok = sequence.toString().toLowerCase();
        if (sequence.charAt(sequence.length() - 1) == '*') {
          tok = sequence.substring(0, sequence.length() - 1).toLowerCase();
        }
        subQuery = new Prefix(query, fieldName, tok, advancedText.getCaseInsensitive());
      } else {
        StringBuilder sequence = token.getExpression();
        if (sequence.length() <= 0) {
          continue;
        }
        subQuery = new com.dropchop.recyclone.base.es.model.query.cond.Match(query, fieldName, sequence.toString());
      }
      if (subQuery == null) {
        continue;
      }

      if (token.isMust()) {
        query.must(subQuery);
      } else if (token.isMustNot()) {
        query.mustNot(subQuery);
      } else {
        query.should(subQuery);
      }
    }
  }

  public MatchText(IQueryNode parent, String fieldName, AdvancedText advancedText) {
    super(parent, fieldName, advancedText.getValue(), true);
    this.slop = advancedText.getSlop();
    this.caseInsensitive = advancedText.getCaseInsensitive();
    Bool query = new Bool(this);
    this.tokens = ExpressionParser.parse(advancedText.getValue(), false, true, true);
    mapAdvancedText(query, this.fieldName, advancedText, this.tokens);
    if (!query.isEmpty()) {
      this.putAll(query);
    }
  }
}
