package com.dropchop.recyclone.base.api.model.query.condition;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.KnnCondition;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 08. 2025
 */
@SuppressWarnings("unused")
public class Knn implements Condition {

  private KnnCondition<?> knn;

  public Knn() {
  }

  public Knn(KnnCondition<?> knn) {
    set$knn(knn);
  }

  public KnnCondition<?> get$knn() {
    return knn;
  }

  public void set$knn(KnnCondition<?> knn) {
    this.knn = knn;
  }

  public Knn knn(KnnCondition<?> knn) {
    this.set$knn(knn);
    return this;
  }
}
