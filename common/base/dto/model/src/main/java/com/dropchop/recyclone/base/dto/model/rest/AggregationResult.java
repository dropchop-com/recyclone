package com.dropchop.recyclone.base.dto.model.rest;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 06. 06. 2025
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor(force = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class AggregationResult {

  @Getter
  @Setter
  @NoArgsConstructor(force = true)
  @JsonInclude(NON_NULL)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Bucket {
    @JsonProperty("key")
    private String key;
    @JsonProperty("doc_count")
    private Long docCount;

    @JsonIgnore
    private Map<String, AggregationResult> subAggregations = new LinkedHashMap<>();

    @JsonAnySetter
    public void setSub(String name, AggregationResult value) {
      if (name == null || value == null) {
        log.warn("Invalid sub-aggregation: {}={}", name, value);
        return;
      }
      subAggregations.put(name, value);
    }

    @JsonAnyGetter
    public Map<String, AggregationResult> getSubs(){
      return subAggregations;
    }
  }

  @JsonProperty("meta")
  Map<String, Object> meta = new LinkedHashMap<>();

  @JsonProperty("buckets")
  private List<Bucket> buckets;

  @JsonProperty("doc_count_error_upper_bound")
  private Long docCountErrorUpperBound;

  @JsonProperty("sum_other_doc_count")
  private Long sumOtherDocCount;

  // For single-value metrics
  @JsonProperty("value")
  private Double value;

  // For stats aggregations
  @JsonProperty("count")
  private Long count;

  @JsonProperty("min")
  private Double min;

  @JsonProperty("max")
  private Double max;

  @JsonProperty("avg")
  private Double avg;

  @JsonProperty("sum")
  private Double sum;
}
