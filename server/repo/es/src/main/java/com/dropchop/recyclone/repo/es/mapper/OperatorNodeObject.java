package com.dropchop.recyclone.repo.es.mapper;

import java.util.Collection;

@SuppressWarnings("unused")
public class OperatorNodeObject extends QueryNodeObject {

  public OperatorNodeObject() {
    super();
  }

  // Handle "eq" operator
  public <T> void addEqOperator(String field, T value) {
    QueryNodeObject termNode = new QueryNodeObject();
    termNode.put(field, value);
    this.put("term", termNode);
  }

  // Handle "gt", "lt", "gte", "lte" range queries
  public <T> void addRangeOperator(String field, String operator, T value) {
    QueryNodeObject rangeNode = new QueryNodeObject();
    QueryNodeObject operatorNode = new QueryNodeObject();
    operatorNode.put(operator, value);
    rangeNode.put(field, operatorNode);
    this.put("range", rangeNode);
  }

  // Handle "in" operator
  public <T> void addInOperator(String field, T values) {
    QueryNodeObject termsNode = new QueryNodeObject();
    termsNode.put(field, values);
    this.put("terms", termsNode);
  }

  // Handle interval operators (ClosedInterval, OpenInterval, etc.)
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
}