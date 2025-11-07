package com.dropchop.recyclone.base.api.model.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 08. 2025
 */
@Getter
@Setter
public class KnnTextCondition extends KnnCondition<String> {

  public KnnTextCondition(String name, String val, Integer topK, Float similarity, Integer numCandidates, Condition filter) {
    super(name, val, topK, similarity, numCandidates, filter);
  }

  public KnnTextCondition(String name, String val, Integer topK, Float similarity, Integer numCandidates) {
    super(name, val, topK, similarity, numCandidates);
  }

  public KnnTextCondition(String name, String val, Integer topK, Float similarity, Condition filter) {
    super(name, val, topK, similarity, filter);
  }

  public KnnTextCondition(String name, String val, Integer topK, Condition filter) {
    super(name, val, topK, filter);
  }

  public KnnTextCondition(String name, String val, Condition filter) {
    super(name, val, filter);
  }

  public KnnTextCondition(String name, String val, Integer topK, Float similarity) {
    super(name, val, topK, similarity);
  }

  public KnnTextCondition(String name, String val, Integer topK) {
    super(name, val, topK);
  }

  public KnnTextCondition(String name, String val) {
    super(name, val);
  }
}
