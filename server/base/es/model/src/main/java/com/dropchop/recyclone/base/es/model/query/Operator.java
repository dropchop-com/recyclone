package com.dropchop.recyclone.base.es.model.query;

import com.dropchop.recyclone.base.api.model.legacy.text.ExpressionToken;
import com.dropchop.recyclone.base.api.model.legacy.text.Filter;
import com.dropchop.recyclone.base.api.model.legacy.text.ExpressionParser;
import com.dropchop.recyclone.base.api.model.legacy.text.Or;
import com.dropchop.recyclone.base.api.model.query.operator.Match;
import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Wildcard;

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

  //TODO: consolidate
  private QueryObject expressionToQueryNode(String defaultField, ExpressionToken token) {
    if (token instanceof Or orToken) {
      Bool orBool = new Bool();
      for (ExpressionToken part : orToken.getExpressionTokens()) {
        QueryObject child = expressionToQueryNode(defaultField, part);
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
      QueryObject matchPhrase = new QueryObject();
      QueryObject conf = new QueryObject();
      conf.put("query", phraseText);
      Map<String, Object> meta = phrase.getMetaData();
      if (meta != null) {
        copyIfPresent(meta, conf, "slop");
        copyIfPresent(meta, conf, "boost");
        copyIfPresent(meta, conf, "analyzer");
      }
      QueryObject wrapper = new QueryObject();
      wrapper.put(field, conf);
      matchPhrase.put("match_phrase", wrapper);
      return matchPhrase;
    }
    String term = token.getExpression().toString();
    if (token.isPrefix() || term.indexOf('*') >= 0) {
      QueryObject wildcard = new QueryObject();
      QueryObject conf = new QueryObject();
      conf.put("value", term);
      conf.put("case_insensitive", true);
      Map<String, Object> meta = token.getMetaData();
      if (meta != null) {
        copyIfPresent(meta, conf, "boost");
      }
      QueryObject wrapper = new QueryObject();
      wrapper.put(field, conf);
      wildcard.put("wildcard", wrapper);
      return wildcard;
    } else {
      QueryObject match = new QueryObject();
      QueryObject conf = new QueryObject();
      conf.put("query", term);
      Map<String, Object> meta = token.getMetaData();
      if (meta != null) {
        copyIfPresent(meta, conf, "fuzziness");
        copyIfPresent(meta, conf, "boost");
        copyIfPresent(meta, conf, "analyzer");
        copyIfPresent(meta, conf, "operator");
      }
      QueryObject wrapper = new QueryObject();
      wrapper.put(field, conf);
      match.put("match", wrapper);
      return match;
    }
  }

  //TODO: consolidate
  private QueryObject mapAdvancedText(String defaultField, String value) {
    List<ExpressionToken> tokens = ExpressionParser.parse(value, false, true, true);

    Bool inner = new Bool();
    for (ExpressionToken token : tokens) {
      QueryObject node = expressionToQueryNode(defaultField, token);
      if (token.isMustNot()) {
        inner.mustNot(node);
      } else if (token.isMust()) {
        inner.must(node);
      } else {
        inner.should(node);
      }
    }
    if (!inner.containsKey("must") && !inner.containsKey("must_not") && !inner.getShould().isEmpty()) {
      inner.setMinimumShouldMatch(1);
    }

    QueryObject wrapper = new QueryObject();
    wrapper.put("bool", inner);
    return wrapper;
  }

  public void addTextSearch(String field, Match<?> match) {
    Object text = match.getText();
    if (text instanceof Wildcard wildcard) {
      //QueryObject wildcardObject = new QueryObject();
      QueryObject nameObject = new QueryObject();
      QueryObject valueObject = new QueryObject();
      QueryObject paramsObject = new QueryObject();
      valueObject.put("value", wildcard.getValue());

      if (wildcard.getBoost() != null) {
        QueryObject boostObject = new QueryObject();
        boostObject.put("boost", wildcard.getBoost());
        paramsObject.putAll(boostObject);
      }

      if (wildcard.getCaseInsensitive() != null) {
        QueryObject caseObject = new QueryObject();
        caseObject.put("case_insensitive", wildcard.getCaseInsensitive());
        paramsObject.putAll(caseObject);
      }

      paramsObject.putAll(valueObject);
      nameObject.put(field, paramsObject);
      this.put("wildcard", nameObject);
    } else if (text instanceof Phrase phrase) {
      QueryObject nameObject = new QueryObject();
      QueryObject paramsObject = new QueryObject();
      QueryObject valueObject = new QueryObject();

      valueObject.put("query", phrase.getValue());
      paramsObject.putAll(valueObject);

      //Cant seem to figure out how phrase.getAnalyzer() returns "null" instead of null
      if(phrase.getAnalyzer() != null && !"null".equals(phrase.getAnalyzer())) {
        QueryObject anaObject = new QueryObject();
        anaObject.put("analyzer", phrase.getAnalyzer());
        paramsObject.putAll(anaObject);
      }

      if (phrase.getSlop() != null) {
        QueryObject caseObject = new QueryObject();
        caseObject.put("slop", phrase.getSlop());
        paramsObject.putAll(caseObject);
      }

      nameObject.put(field, paramsObject);
      this.put("match_phrase", nameObject);
    } else if(text instanceof AdvancedText advancedText) {
      /*List<String> tokens = new TextParser(advancedText.getValue()).parse();

      ClauseFactory factory = new ClauseFactory(field);
      SpanNear spanNear = new SpanNear(
          null, advancedText.getSlop(), advancedText.getInOrder()
      );

      for (String token : tokens) {
        QueryObject clause = factory.createClause(token, advancedText.getCaseInsensitive());
        clause.setParent(spanNear);
        spanNear.addClause(clause);
      }

      this.put("span_near", spanNear);*/
      QueryObject advancedQuery = mapAdvancedText(field, advancedText.getValue());
      if (!advancedQuery.isEmpty()) {
        //Operator wrapper = new Operator();
        //wrapper.putAll(advancedQuery);
        //TODO: fix
        //return wrapper;
        this.putAll(advancedQuery);
      }
    }
  }
}