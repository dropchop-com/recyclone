package com.dropchop.recyclone.base.api.repo.mapper;

@SuppressWarnings("unused")
public class OperatorNodeObject extends QueryNodeObject {

  public OperatorNodeObject() {
    super();
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

  public <T> void addNullSearch(String field) {
    QueryNodeObject termNode = new QueryNodeObject();
    QueryNodeObject searchNode = new QueryNodeObject();
    QueryNodeObject nullNode = new QueryNodeObject();
    termNode.put("field", field);
    searchNode.put("exists", termNode);
    nullNode.put("must_not", searchNode);
    this.put("bool", nullNode);
  }

}