package com.dropchop.recyclone.base.dto.model.rest;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * A single aggregation result.
 * Can be used for both single-bucket and multi-bucket aggregations and is
 * directly compatible with the Elasticsearch response format.
 *
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
  public static class Container {

    @JsonProperty("key")
    private String key;

    @JsonProperty("doc_count")
    private Long docCount;

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("lang")
    private String lang;

    @JsonIgnore
    private Map<String, AggregationResult> subAggregations = new LinkedHashMap<>();

    /**
     * Set a named sub-aggregation.
     */
    @JsonAnySetter
    public void setSub(String name, AggregationResult value) {
      if (name == null || value == null) {
        log.warn("Invalid sub-aggregation: {}={}", name, value);
        return;
      }
      subAggregations.put(name, value);
    }

    /**
     * Get all named sub-aggregations, or an empty map if there are none.
     */
    @JsonAnyGetter
    public Map<String, AggregationResult> getSubs(){
      return subAggregations;
    }
  }

  @JsonProperty("meta")
  @JsonInclude(NON_EMPTY)
  Map<String, Object> meta = new LinkedHashMap<>();

  @JsonProperty("buckets")
  private List<Container> containers;

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
