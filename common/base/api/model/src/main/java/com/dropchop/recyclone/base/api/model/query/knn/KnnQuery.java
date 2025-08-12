package com.dropchop.recyclone.base.api.model.query.knn;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnnQuery {

  @NonNull
  @JsonProperty("field")
  private String field;

  @JsonProperty("query_vector")
  private float[] queryVector;

  @JsonProperty("k")
  private Integer k;

  @JsonProperty("num_candidates")
  private Integer numCandidates;

  @JsonProperty("filter")
  private Condition filter;

  @JsonProperty("similarity")
  private Float similarity;

  @JsonProperty("boost")
  @Builder.Default
  private Float boost = 1.0f;

  @JsonProperty("name")
  private String name;

  public static KnnQuery of(String field, float[] queryVector) {
    return KnnQuery.builder()
      .field(field)
      .queryVector(queryVector)
      .build();
  }

  public static KnnQuery of(String field, float[] queryVector, int k) {
    return KnnQuery.builder()
      .field(field)
      .queryVector(queryVector)
      .k(k)
      .build();
  }

  public static KnnQuery of(String field, float[] queryVector, int k, int numCandidates) {
    return KnnQuery.builder()
      .field(field)
      .queryVector(queryVector)
      .k(k)
      .numCandidates(numCandidates)
      .build();
  }
}
