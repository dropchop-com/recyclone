package com.dropchop.recyclone.base.api.model.query.condition;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.KnnField;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 08. 2025
 */
@SuppressWarnings("unused")
public class Knn implements Condition {

  private KnnField knn;

  public Knn() {
  }

  public Knn(KnnField knn) {
    set$knn(knn);
  }

  public KnnField get$knn() {
    return knn;
  }

  public void set$knn(KnnField knn) {
    this.knn = knn;
  }

  public Knn knn(KnnField knn) {
    this.set$knn(knn);
    return this;
  }
}
