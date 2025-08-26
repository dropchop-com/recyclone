package com.dropchop.recyclone.base.api.model.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 08. 2025
 */
@Getter
@Setter
public class KnnField implements Condition {
  private String name;
  private float[] value;
  private Integer topK;
  private Float similarity;
  private Integer numCandidates;
  private Condition filter;

  public KnnField(String name, float[] val, Integer topK, Float similarity, Integer numCandidates, Condition filter) {
    this.name = name;
    this.value = val;
    this.topK = topK;
    this.similarity = similarity;
    this.numCandidates = numCandidates;
    this.filter = filter;
  }

  public KnnField(String name, float[] val, Integer topK, Float similarity,  Integer numCandidates) {
    this(name, val, topK, similarity, numCandidates, null);
  }

  public KnnField(String name, float[] val, Integer topK, Float similarity, Condition filter) {
    this(name, val, topK, similarity, null, filter);
  }

  public KnnField(String name, float[] val, Integer topK, Condition filter) {
    this(name, val, topK, null, null, filter);
  }

  public KnnField(String name, float[] val, Condition filter) {
    this(name, val, null, null, null, filter);
  }

  public KnnField(String name, float[] val, Integer topK, Float similarity) {
    this(name, val, topK, similarity, null, null);
  }

  public KnnField(String name, float[] val, Integer topK) {
    this(name, val, topK, null, null, null);
  }

  public KnnField(String name, float[] val) {
    this(name, val, null, null, null, null);
  }
}
