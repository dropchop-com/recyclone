package com.dropchop.recyclone.repo.es.mapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticSearchResult<T> {
  @JsonProperty("hits")
  private Hits<T> hits;

  @Setter
  @Getter
  public static class Hits<T> {
    @JsonProperty("hits")
    private List<Hit<T>> hits;

  }

  @Setter
  @Getter
  public static class Hit<T> {
    @JsonProperty("_source")
    private T source;

  }
}