package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.api.model.legacy.text.ExpressionParser;
import com.dropchop.recyclone.base.api.model.legacy.text.ExpressionToken;
import com.dropchop.recyclone.base.api.model.legacy.text.Or;
import com.dropchop.recyclone.base.api.model.query.operator.Match;
import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Wildcard;
import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.es.model.query.QueryObject;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Operator extends QueryObject {

  public Operator() {
    super();
  }

  public Operator(IQueryNode parent) {
    super(parent);
  }

  public <T> void addEqOperator(String field, T value) {
    QueryObject termNode = new QueryObject();
    termNode.put(field, value);
    this.put("term", termNode);
  }

  public <T> void addRangeOperator(String field, String operator, T value) {
    QueryObject rangeNode = new QueryObject();
    QueryObject operatorNode = new QueryObject();
    operatorNode.put(operator, value);
    rangeNode.put(field, operatorNode);
    this.put("range", rangeNode);
  }

  public <T> void addInOperator(String field, T values) {
    QueryObject termsNode = new QueryObject();
    termsNode.put(field, values);
    this.put("terms", termsNode);
  }

  public <T> void addClosedInterval(String field, T from, T to) {
    QueryObject rangeNode = new QueryObject();
    QueryObject intervalNode = new QueryObject();
    intervalNode.put("gte", from);
    intervalNode.put("lte", to);
    rangeNode.put(field, intervalNode);
    this.put("range", rangeNode);
  }

  public <T> void addOpenInterval(String field, T from, T to) {
    QueryObject rangeNode = new QueryObject();
    QueryObject intervalNode = new QueryObject();
    intervalNode.put("gt", from);
    intervalNode.put("lt", to);
    rangeNode.put(field, intervalNode);
    this.put("range", rangeNode);
  }

  public <T> void addClosedOpenInterval(String field, T from, T to) {
    QueryObject rangeNode = new QueryObject();
    QueryObject intervalNode = new QueryObject();
    intervalNode.put("gte", from);
    intervalNode.put("lt", to);
    rangeNode.put(field, intervalNode);
    this.put("range", rangeNode);
  }

  public <T> void addOpenClosedInterval(String field, T from, T to) {
    QueryObject rangeNode = new QueryObject();
    QueryObject intervalNode = new QueryObject();
    intervalNode.put("gt", from);
    intervalNode.put("lte", to);
    rangeNode.put(field, intervalNode);
    this.put("range", rangeNode);
  }

  public void addNullSearch(String field) {
    QueryObject termNode = new QueryObject();
    QueryObject searchNode = new QueryObject();
    QueryObject nullNode = new QueryObject();
    termNode.put("field", field);
    searchNode.put("exists", termNode);
    nullNode.put("must_not", searchNode);
    this.put("bool", nullNode);
  }

  private void copyIfPresent(Map<String, Object> meta, QueryObject to, String key) {
    Object v = meta.get(key);
    if (v != null) to.put(key, v);
  }

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


  public void addTextSearch(String field, Match<?> match) {
    Object text = match.getText();
    if (text instanceof Wildcard wildcard) {
      this.putAll(
          new com.dropchop.recyclone.base.es.model.query.cond.Wildcard(
              this, field, wildcard.getValue(), wildcard.getCaseInsensitive(), wildcard.getBoost()
          )
      );
    } else if (text instanceof Phrase phrase) {

      QueryObject paramsObject = new QueryObject();
      QueryObject valueObject = new QueryObject();
      valueObject.put("query", phrase.getValue());
      paramsObject.putAll(valueObject);

      if(phrase.getAnalyzer() != null) {
        QueryObject anaObject = new QueryObject();
        anaObject.put("analyzer", phrase.getAnalyzer());
        paramsObject.putAll(anaObject);
      }

      if (phrase.getSlop() != null) {
        QueryObject caseObject = new QueryObject();
        caseObject.put("slop", phrase.getSlop());
        paramsObject.putAll(caseObject);
      }

      QueryObject nameObject = new QueryObject();
      nameObject.put(field, paramsObject);
      this.put("match_phrase", nameObject);
    } else if(text instanceof AdvancedText advancedText) {
      String value = advancedText.getValue();
      Bool query = new Bool(this);
      List<ExpressionToken> tokens = ExpressionParser.parse(value, false, true, true);
      mapAdvancedText(query, field, advancedText, tokens);
      if (!query.isEmpty()) {
        this.putAll(query);
      }
    }
  }
}