package com.dropchop.recyclone.base.api.model.query;

import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.condition.Knn;
import com.dropchop.recyclone.base.api.model.query.condition.Not;
import com.dropchop.recyclone.base.api.model.query.condition.Or;
import com.dropchop.recyclone.base.api.model.query.operator.Match;
import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Wildcard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@SuppressWarnings("unused")
public interface Condition {

  static Map<String, Class<? extends Condition>> supported() {
    return Map.of(
      "and", And.class,
      "or", Or.class,
      "not", Not.class,
      "field", Field.class,
      "conditionedField", ConditionedField.class,
      "knn", Knn.class
    );
  }

  static And and(Condition... conditions) {
    return new And(new ArrayList<>(Arrays.asList(conditions)));
  }

  static And and(List<Condition> conditions) {
    return new And(conditions);
  }

  static Or or(Condition... conditions) {
    return new Or(new ArrayList<>(Arrays.asList(conditions)));
  }

  static Or or(List<Condition> conditions) {
    return new Or(conditions);
  }

  static Not not(Condition condition) {
    return new Not(condition);
  }

  static ConditionedField field(String name, ConditionOperator operator) {
    return new ConditionedField(name, operator);
  }

  static <T> Field<T> field(String name, T value) {
    return new Field<>(name, value);
  }

  static Knn knn(String field, float[] value, Integer topK, Float similarity, Integer numCandidates) {
    return new Knn(new KnnEmbeddingCondition(field, value, topK, similarity, numCandidates));
  }

  static Knn knn(String field, String value, Integer topK, Float similarity, Integer numCandidates) {
    return new Knn(new KnnTextCondition(field, value, topK, similarity, numCandidates));
  }

  static Knn knn(String field, float[] value, Integer topK, Float similarity) {
    return new Knn(new KnnEmbeddingCondition(field, value, topK, similarity));
  }

  static Knn knn(String field, String value, Integer topK, Float similarity) {
    return new Knn(new KnnTextCondition(field, value, topK, similarity));
  }

  static Knn knn(String field, float[] value, Integer topK) {
    return new Knn(new KnnEmbeddingCondition(field, value, topK));
  }

  static Knn knn(String field, String value, Integer topK) {
    return new Knn(new KnnTextCondition(field, value, topK));
  }

  static Knn knn(String field, float[] value) {
    return new Knn(new KnnEmbeddingCondition(field, value));
  }

  static Knn knn(String field, String value) {
    return new Knn(new KnnTextCondition(field, value));
  }

  static Knn knn(String field, float[] value, Integer topK, Float similarity, Integer numCandidates, Condition filter) {
    return new Knn(new KnnEmbeddingCondition(field, value, topK, similarity, numCandidates, filter));
  }

  static Knn knn(String field, String value, Integer topK, Float similarity, Integer numCandidates, Condition filter) {
    return new Knn(new KnnTextCondition(field, value, topK, similarity, numCandidates, filter));
  }

  static Knn knn(String field, float[] value, Integer topK, Float similarity, Condition filter) {
    return new Knn(new KnnEmbeddingCondition(field, value, topK, similarity, filter));
  }

  static Knn knn(String field, String value, Integer topK, Float similarity, Condition filter) {
    return new Knn(new KnnTextCondition(field, value, topK, similarity, filter));
  }

  static Knn knn(String field, float[] value, Integer topK, Condition filter) {
    return new Knn(new KnnEmbeddingCondition(field, value, topK, filter));
  }

  static Knn knn(String field, String value, Integer topK, Condition filter) {
    return new Knn(new KnnTextCondition(field, value, topK, filter));
  }

  static Knn knn(String field, float[] value, Condition filter) {
    return new Knn(new KnnEmbeddingCondition(field, value, filter));
  }

  static Knn knn(String field, String value, Condition filter) {
    return new Knn(new KnnTextCondition(field, value, filter));
  }

  static ConditionedField advancedText(String name, String value) {
    return new ConditionedField(name, new Match<>(new AdvancedText(value)));
  }

  static ConditionedField advancedText(String name, String value, Integer slop) {
    return new ConditionedField(name, new Match<>(new AdvancedText(value, slop)));
  }

  static ConditionedField advancedText(String name, String value, Boolean caseInsensitive) {
    return new ConditionedField(name, new Match<>(new AdvancedText(value, caseInsensitive)));
  }

  static ConditionedField advancedText(String name, String value, Boolean caseInsensitive, Integer slop) {
    return new ConditionedField(name, new Match<>(new AdvancedText(value, caseInsensitive, slop)));
  }

  static ConditionedField advancedText(String name, String value, Boolean caseInsensitive, Integer slop, Boolean inOrder) {
    return new ConditionedField(name, new Match<>(new AdvancedText(value, caseInsensitive, slop, inOrder)));
  }

  static ConditionedField phrase(String name, String value) {
    return new ConditionedField(name, new Match<>(new Phrase(value)));
  }

  static ConditionedField phrase(String name, String value, Integer slop) {
    return new ConditionedField(name, new Match<>(new Phrase(value, slop)));
  }

  static ConditionedField phrase(String name, String value, String analyzer) {
    return new ConditionedField(name, new Match<>(new Phrase(value, analyzer)));
  }

  static ConditionedField phrase(String name, String value, String analyzer, Integer slop) {
    return new ConditionedField(name, new Match<>(new Phrase(value, analyzer, slop)));
  }

  static ConditionedField wildcard(String name, String value) {
    return new ConditionedField(name, new Match<>(new Wildcard(value)));
  }

  static ConditionedField wildcard(String name, String value, Boolean caseInsensitive) {
    return new ConditionedField(name, new Match<>(new Wildcard(value, caseInsensitive)));
  }

  static ConditionedField wildcard(String name, String value, Float boost) {
    return new ConditionedField(name, new Match<>(new Wildcard(value, boost)));
  }

  static ConditionedField wildcard(String name, String value, Boolean caseInsensitive, Float boost) {
    return new ConditionedField(name, new Match<>(new Wildcard(value, caseInsensitive, boost)));
  }
}
