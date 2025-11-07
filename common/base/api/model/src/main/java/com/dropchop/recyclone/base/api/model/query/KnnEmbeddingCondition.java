package com.dropchop.recyclone.base.api.model.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 08. 2025
 */
@Getter
@Setter
public class KnnEmbeddingCondition extends KnnCondition<float[]> {

  public KnnEmbeddingCondition(String name, float[] val, Integer topK, Float similarity, Integer numCandidates, Condition filter) {
    super(name, val, topK, similarity, numCandidates, filter);
  }

  public KnnEmbeddingCondition(String name, float[] val, Integer topK, Float similarity, Integer numCandidates) {
    super(name, val, topK, similarity, numCandidates);
  }

  public KnnEmbeddingCondition(String name, float[] val, Integer topK, Float similarity, Condition filter) {
    super(name, val, topK, similarity, filter);
  }

  public KnnEmbeddingCondition(String name, float[] val, Integer topK, Condition filter) {
    super(name, val, topK, filter);
  }

  public KnnEmbeddingCondition(String name, float[] val, Condition filter) {
    super(name, val, filter);
  }

  public KnnEmbeddingCondition(String name, float[] val, Integer topK, Float similarity) {
    super(name, val, topK, similarity);
  }

  public KnnEmbeddingCondition(String name, float[] val, Integer topK) {
    super(name, val, topK);
  }

  public KnnEmbeddingCondition(String name, float[] val) {
    super(name, val);
  }
}
