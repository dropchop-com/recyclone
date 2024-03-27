package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 31. 08. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class ResultFilter
  implements com.dropchop.recyclone.model.api.invoke.ResultFilter<
  ResultFilter.ContentFilter,
  ResultFilter.LanguageFilter
  > {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(NON_NULL)
  public static class ContentFilter
    implements Dto, com.dropchop.recyclone.model.api.invoke.ResultFilter.ContentFilter {

    @JsonInclude(NON_EMPTY)
    private List<String> includes = new ArrayList<>();

    @JsonInclude(NON_EMPTY)
    private List<String> excludes = new ArrayList<>();

    private Integer treeLevel;
    private String detailLevel;

    @JsonIgnore
    public ContentFilter include(String include) {
      includes.add(include);
      return this;
    }

    @JsonIgnore
    public ContentFilter exclude(String include) {
      excludes.add(include);
      return this;
    }

    @JsonIgnore
    public ContentFilter includes(List<String> includes) {
      this.includes = includes;
      return this;
    }

    @JsonIgnore
    public ContentFilter excludes(List<String> excludes) {
      this.excludes = excludes;
      return this;
    }

    @JsonIgnore
    public ContentFilter treeLevel(Integer treeLevel) {
      this.treeLevel = treeLevel;
      return this;
    }

    @JsonIgnore
    public ContentFilter detailLevel(String detailLevel) {
      this.detailLevel = detailLevel;
      return this;
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(NON_NULL)
  public static class LanguageFilter
    implements Dto, com.dropchop.recyclone.model.api.invoke.ResultFilter.LanguageFilter {
    private String search;
    private String translation;

    public LanguageFilter search(String search) {
      this.search = search;
      return this;
    }

    public LanguageFilter translation(String translation) {
      this.translation = translation;
      return this;
    }
  }

  private ContentFilter content = new ContentFilter();

  private LanguageFilter lang = new LanguageFilter();

  private int size = 100;

  private int from = 0;

  private String version = null;

  @Singular
  @JsonInclude(NON_EMPTY)
  private List<String> states = new ArrayList<>();

  @Singular("sort")
  @JsonInclude(NON_EMPTY)
  private List<String> sort = new ArrayList<>();

  public ResultFilter content(ContentFilter contentFilter) {
    this.content = contentFilter;
    return this;
  }

  public ResultFilter lang(LanguageFilter languageFilter) {
    this.lang = languageFilter;
    return this;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
