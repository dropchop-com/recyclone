package com.dropchop.recyclone.base.es.repo.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticSearchResult<T> {

  @JsonProperty("hits")
  private Hits<T> hits;

  @JsonProperty("aggregations")
  private Map<String, Object> aggregations;

  @Setter
  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Hits<T> {

    @JsonProperty("total")
    private Total total;

    @JsonProperty("max_score")
    private Float maxScore;

    @JsonProperty("hits")
    private List<Hit<T>> hits;
  }

  @Setter
  @Getter
  public static class Total {
    @JsonProperty("value")
    private int value;

    @JsonProperty("relation")
    private String relation;
  }

  @Setter
  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Hit<T> {
    @JsonProperty("_source")
    private T source;

    @JsonProperty("sort")
    private List<Object> sort;
  }

  @Setter
  @Getter
  public static class Container<T extends Hit<?>> {
    private T hit;

    public boolean isEmpty() {
      return hit == null;
    }
  }
}
