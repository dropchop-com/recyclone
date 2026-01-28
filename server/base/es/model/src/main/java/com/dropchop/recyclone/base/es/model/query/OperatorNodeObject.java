package com.dropchop.recyclone.base.es.model.query;

import com.dropchop.recyclone.base.api.model.legacy.text.ExpressionToken;
import com.dropchop.recyclone.base.api.model.legacy.text.Filter;
import com.dropchop.recyclone.base.api.model.legacy.text.LegacyExpressionParser;
import com.dropchop.recyclone.base.api.model.legacy.text.Or;
import com.dropchop.recyclone.base.api.model.query.operator.Match;
import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Wildcard;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class OperatorNodeObject extends QueryNodeObject {

  public OperatorNodeObject() {
    super();
  }

  public OperatorNodeObject(IQueryNode parent) {
    super(parent);
  }

  public <T> void addEqOperator(String field, T value) {
    QueryNodeObject termNode = new QueryNodeObject();
    termNode.put(field, value);
    this.put("term", termNode);
  }

  public <T> void addRangeOperator(String field, String operator, T value) {
    QueryNodeObject rangeNode = new QueryNodeObject();
    QueryNodeObject operatorNode = new QueryNodeObject();
    operatorNode.put(operator, value);
    rangeNode.put(field, operatorNode);
    this.put("range", rangeNode);
  }

  public <T> void addInOperator(String field, T values) {
    QueryNodeObject termsNode = new QueryNodeObject();
    termsNode.put(field, values);
    this.put("terms", termsNode);
  }

  public <T> void addClosedInterval(String field, T from, T to) {
    QueryNodeObject rangeNode = new QueryNodeObject();
    QueryNodeObject intervalNode = new QueryNodeObject();
    intervalNode.put("gte", from);
    intervalNode.put("lte", to);
    rangeNode.put(field, intervalNode);
    this.put("range", rangeNode);
  }

  public <T> void addOpenInterval(String field, T from, T to) {
    QueryNodeObject rangeNode = new QueryNodeObject();
    QueryNodeObject intervalNode = new QueryNodeObject();
    intervalNode.put("gt", from);
    intervalNode.put("lt", to);
    rangeNode.put(field, intervalNode);
    this.put("range", rangeNode);
  }

  public <T> void addClosedOpenInterval(String field, T from, T to) {
    QueryNodeObject rangeNode = new QueryNodeObject();
    QueryNodeObject intervalNode = new QueryNodeObject();
    intervalNode.put("gte", from);
    intervalNode.put("lt", to);
    rangeNode.put(field, intervalNode);
    this.put("range", rangeNode);
  }

  public <T> void addOpenClosedInterval(String field, T from, T to) {
    QueryNodeObject rangeNode = new QueryNodeObject();
    QueryNodeObject intervalNode = new QueryNodeObject();
    intervalNode.put("gt", from);
    intervalNode.put("lte", to);
    rangeNode.put(field, intervalNode);
    this.put("range", rangeNode);
  }

  public void addNullSearch(String field) {
    QueryNodeObject termNode = new QueryNodeObject();
    QueryNodeObject searchNode = new QueryNodeObject();
    QueryNodeObject nullNode = new QueryNodeObject();
    termNode.put("field", field);
    searchNode.put("exists", termNode);
    nullNode.put("must_not", searchNode);
    this.put("bool", nullNode);
  }

  private void copyIfPresent(Map<String, Object> meta, QueryNodeObject to, String key) {
    Object v = meta.get(key);
    if (v != null) to.put(key, v);
  }

  //TODO: consolidate
  private QueryNodeObject expressionToQueryNode(String defaultField, ExpressionToken token) {
    if (token instanceof Or orToken) {
      BoolQueryObject orBool = new BoolQueryObject();
      for (ExpressionToken part : orToken.getExpressionTokens()) {
        QueryNodeObject child = expressionToQueryNode(defaultField, part);
        orBool.should(child);
      }
      orBool.setMinimumShouldMatch(1);
      return orBool;
    }

    String field = defaultField;
    if (token instanceof Filter filter && filter.getName() != null && !filter.getName().isEmpty()) {
      field = filter.getName();
    }

    if (token instanceof com.dropchop.recyclone.base.api.model.legacy.text.Phrase phrase) {
      String phraseText = phrase.getExpression().toString();
      QueryNodeObject matchPhrase = new QueryNodeObject();
      QueryNodeObject conf = new QueryNodeObject();
      conf.put("query", phraseText);
      Map<String, Object> meta = phrase.getMetaData();
      if (meta != null) {
        copyIfPresent(meta, conf, "slop");
        copyIfPresent(meta, conf, "boost");
        copyIfPresent(meta, conf, "analyzer");
      }
      QueryNodeObject wrapper = new QueryNodeObject();
      wrapper.put(field, conf);
      matchPhrase.put("match_phrase", wrapper);
      return matchPhrase;
    }
    String term = token.getExpression().toString();
    if (token.isPrefix() || term.indexOf('*') >= 0) {
      QueryNodeObject wildcard = new QueryNodeObject();
      QueryNodeObject conf = new QueryNodeObject();
      conf.put("value", term);
      conf.put("case_insensitive", true);
      Map<String, Object> meta = token.getMetaData();
      if (meta != null) {
        copyIfPresent(meta, conf, "boost");
      }
      QueryNodeObject wrapper = new QueryNodeObject();
      wrapper.put(field, conf);
      wildcard.put("wildcard", wrapper);
      return wildcard;
    } else {
      QueryNodeObject match = new QueryNodeObject();
      QueryNodeObject conf = new QueryNodeObject();
      conf.put("query", term);
      Map<String, Object> meta = token.getMetaData();
      if (meta != null) {
        copyIfPresent(meta, conf, "fuzziness");
        copyIfPresent(meta, conf, "boost");
        copyIfPresent(meta, conf, "analyzer");
        copyIfPresent(meta, conf, "operator");
      }
      QueryNodeObject wrapper = new QueryNodeObject();
      wrapper.put(field, conf);
      match.put("match", wrapper);
      return match;
    }
  }

  //TODO: consolidate
  private QueryNodeObject mapAdvancedText(String defaultField, String value) {
    List<ExpressionToken> tokens = LegacyExpressionParser.parse(value, false, true, true);

    BoolQueryObject inner = new BoolQueryObject();
    for (ExpressionToken token : tokens) {
      QueryNodeObject node = expressionToQueryNode(defaultField, token);
      if (token.isMustNot()) {
        inner.mustNot(node);
      } else if (token.isMust()) {
        inner.must(node);
      } else {
        inner.should(node);
      }
    }
    if (!inner.containsKey("must") && !inner.containsKey("must_not") && inner.getShould() != null) {
      inner.setMinimumShouldMatch(1);
    }

    QueryNodeObject wrapper = new QueryNodeObject();
    wrapper.put("bool", inner);
    return wrapper;
  }

  public void addTextSearch(String field, Match<?> match) {
    Object text = match.getText();
    if (text instanceof Wildcard wildcard) {
      //QueryNodeObject wildcardObject = new QueryNodeObject();
      QueryNodeObject nameObject = new QueryNodeObject();
      QueryNodeObject valueObject = new QueryNodeObject();
      QueryNodeObject paramsObject = new QueryNodeObject();
      valueObject.put("value", wildcard.getValue());

      if (wildcard.getBoost() != null) {
        QueryNodeObject boostObject = new QueryNodeObject();
        boostObject.put("boost", wildcard.getBoost());
        paramsObject.putAll(boostObject);
      }

      if (wildcard.getCaseInsensitive() != null) {
        QueryNodeObject caseObject = new QueryNodeObject();
        caseObject.put("case_insensitive", wildcard.getCaseInsensitive());
        paramsObject.putAll(caseObject);
      }

      paramsObject.putAll(valueObject);
      nameObject.put(field, paramsObject);
      this.put("wildcard", nameObject);
    } else if (text instanceof Phrase phrase) {
      QueryNodeObject nameObject = new QueryNodeObject();
      QueryNodeObject paramsObject = new QueryNodeObject();
      QueryNodeObject valueObject = new QueryNodeObject();

      valueObject.put("query", phrase.getValue());
      paramsObject.putAll(valueObject);

      //Cant seem to figure out how phrase.getAnalyzer() returns "null" instead of null
      if(phrase.getAnalyzer() != null && !"null".equals(phrase.getAnalyzer())) {
        QueryNodeObject anaObject = new QueryNodeObject();
        anaObject.put("analyzer", phrase.getAnalyzer());
        paramsObject.putAll(anaObject);
      }

      if (phrase.getSlop() != null) {
        QueryNodeObject caseObject = new QueryNodeObject();
        caseObject.put("slop", phrase.getSlop());
        paramsObject.putAll(caseObject);
      }

      nameObject.put(field, paramsObject);
      this.put("match_phrase", nameObject);
    } else if(text instanceof AdvancedText advancedText) {
      /*List<String> tokens = new TextParser(advancedText.getValue()).parse();

      ClauseFactory factory = new ClauseFactory(field);
      SpanNearObject spanNear = new SpanNearObject(
          null, advancedText.getSlop(), advancedText.getInOrder()
      );

      for (String token : tokens) {
        QueryNodeObject clause = factory.createClause(token, advancedText.getCaseInsensitive());
        clause.setParent(spanNear);
        spanNear.addClause(clause);
      }

      this.put("span_near", spanNear);*/
      QueryNodeObject advancedQuery = mapAdvancedText(field, advancedText.getValue());
      if (!advancedQuery.isEmpty()) {
        //OperatorNodeObject wrapper = new OperatorNodeObject();
        //wrapper.putAll(advancedQuery);
        //TODO: fix
        //return wrapper;
        this.putAll(advancedQuery);
      }
    }
  }
}