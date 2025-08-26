package com.dropchop.recyclone.base.api.model.query.knn;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@SuppressWarnings("unused")
public class Knn implements Condition {

  private String field;
  private float[] queryVector;
  private Integer k;
  private Condition filter;
  private Float similarity;

  public static Knn of(String field, float[] queryVector) {
    return new Knn(field, queryVector, null, null, null);
  }

  public static Knn of(String field, float[] queryVector, Integer k) {
    return new Knn(field, queryVector, k, null, null);
  }

  public static Knn of(String field, float[] queryVector, Integer k, Condition filter) {
    return new Knn(field, queryVector, k, filter, null);
  }

  public static Knn of(String field, float[] queryVector, Integer k, Condition filter, Float similarity) {
    return new Knn(field, queryVector, k, filter, similarity);
  }

  public Knn get$knn() {
    /*java.util.Map<String, Object> knnMap = new java.util.LinkedHashMap<>();
    knnMap.put("field", this.field);
    knnMap.put("query_vector", this.queryVector);
    if (this.k != null) {
      knnMap.put("k", this.k);
    }
    if (this.filter != null) {
      knnMap.put("filter", this.filter);
    }
    if (this.similarity != null) {
      knnMap.put("similarity", this.similarity);
    }*/
    return new Knn(this.field, this.queryVector, this.k, this.filter, this.similarity);
  }

  public void set$knn(Knn knn) {
    /*if (knn instanceof Knn knnObj) {
      this.field = knnObj.getField();
      this.queryVector = knnObj.getQueryVector();
      this.k = knnObj.getK();
      this.filter = knnObj.getFilter();
      this.similarity = knnObj.getSimilarity();
    }*/
    this.field = knn.field;
    this.queryVector = knn.queryVector;
    this.k = knn.k;
    this.filter = knn.filter;
    this.similarity = knn.similarity;
  }
}
