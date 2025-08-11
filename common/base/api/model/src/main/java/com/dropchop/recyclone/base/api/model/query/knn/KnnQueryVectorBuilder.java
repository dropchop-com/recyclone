package com.dropchop.recyclone.base.api.model.query.knn;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnnQueryVectorBuilder {

  @JsonProperty("text_embedding")
  private TextEmbedding textEmbedding;

  @Data
  @SuperBuilder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class TextEmbedding {
    @JsonProperty("model_text")
    private String modelText;

    @JsonProperty("model_id")
    private String modelId;
  }
}
