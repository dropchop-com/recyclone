package com.dropchop.recyclone.base.api.model.query.knn;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnnRescoreVector {

  @JsonProperty("oversample")
  private Float oversample;

  public static KnnRescoreVector of(float oversample) {
    return KnnRescoreVector.builder()
      .oversample(oversample)
      .build();
  }

  public static KnnRescoreVector disabled() {
    return KnnRescoreVector.builder()
      .oversample(0.0f)
      .build();
  }
}
