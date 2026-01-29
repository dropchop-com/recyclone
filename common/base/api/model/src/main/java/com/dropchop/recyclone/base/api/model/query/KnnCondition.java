package com.dropchop.recyclone.base.api.model.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 07. 11. 2025.
 */
@Getter
@Setter
@JsonInclude(NON_NULL)
public class KnnCondition<T> implements Condition {
  private String name;
  private T value;
  private Integer topK;
  private Float similarity;
  private Integer numCandidates;
  private Condition filter;

  public KnnCondition(String name, T val, Integer topK, Float similarity, Integer numCandidates, Condition filter) {
    this.name = name;
    this.value = val;
    this.topK = topK;
    this.similarity = similarity;
    this.numCandidates = numCandidates;
    this.filter = filter;
  }

  public KnnCondition(String name, T val, Integer topK, Float similarity, Integer numCandidates) {
    this(name, val, topK, similarity, numCandidates, null);
  }

  public KnnCondition(String name, T val, Integer topK, Float similarity, Condition filter) {
    this(name, val, topK, similarity, null, filter);
  }

  public KnnCondition(String name, T val, Integer topK, Condition filter) {
    this(name, val, topK, null, null, filter);
  }

  public KnnCondition(String name, T val, Condition filter) {
    this(name, val, null, null, null, filter);
  }

  public KnnCondition(String name, T val, Integer topK, Float similarity) {
    this(name, val, topK, similarity, null, null);
  }

  public KnnCondition(String name, T val, Integer topK) {
    this(name, val, topK, null, null, null);
  }

  public KnnCondition(String name, T val) {
    this(name, val, null, null, null, null);
  }
}
