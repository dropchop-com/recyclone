package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 08. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class ResultFilter
  implements com.dropchop.recyclone.base.api.model.invoke.ResultFilter<
    ResultFilter.ContentFilter,
    ResultFilter.LanguageFilter
    > {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(NON_NULL)
  public static class ContentFilter
    implements Dto, com.dropchop.recyclone.base.api.model.invoke.ResultFilter.ContentFilter {

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
    public ContentFilter exclude(String exclude) {
      excludes.add(exclude);
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

    /**
     * Syntactic sugar starting point.
     */
    public static ContentFilter cf() {
      return new ContentFilter();
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(NON_NULL)
  public static class LanguageFilter
    implements Dto, com.dropchop.recyclone.base.api.model.invoke.ResultFilter.LanguageFilter {
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

    /**
     * Syntactic sugar starting point.
     */
    @SuppressWarnings("unused")
    public static LanguageFilter lf() {
      return new LanguageFilter();
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
    this.setLang(languageFilter);
    return this;
  }

  @Override
  public ResultFilter size(int size) {
    this.setSize(size);
    return this;
  }

  @Override
  public ResultFilter from(int from) {
    this.setFrom(from);
    return this;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  /**
   * Syntactic sugar chaining starting point.
   */
  public static ResultFilter rf() {
    return new ResultFilter();
  }

  public static ResultFilter copy(com.dropchop.recyclone.base.api.model.invoke.ResultFilter<?, ?> rf,
                                  com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.ContentFilter cfDefault,
                                  com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.LanguageFilter lfDefault) {
    ResultFilter copyRf = new ResultFilter();
    if (rf != null && rf.getContent() != null) {
      com.dropchop.recyclone.base.api.model.invoke.ResultFilter.ContentFilter cf = rf.getContent();
      copyRf.setContent(
          new ContentFilter(
              cf.getIncludes(),
              cf.getExcludes(),
              cf.getTreeLevel(),
              cf.getDetailLevel()
          )
      );
    } else if (cfDefault != null) {
      copyRf.setContent(cfDefault);
    }
    if (rf != null && rf.getLang() != null) {
      copyRf.setLang(new LanguageFilter(rf.getLang().getSearch(), rf.getLang().getTranslation()));
    } else if (lfDefault != null) {
      copyRf.setLang(lfDefault);
    }
    if (rf != null) {
      copyRf.setSize(rf.getSize());
      copyRf.setFrom(rf.getFrom());
      copyRf.setVersion(rf.getVersion());
      copyRf.getStates().addAll(rf.getStates());
    }
    return copyRf;
  }

  public static ResultFilter copy(com.dropchop.recyclone.base.api.model.invoke.ResultFilter<?, ?> rf,
                                  com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.ContentFilter cfDefault) {
    return copy(rf, cfDefault, null);
  }

  public static ResultFilter copy(com.dropchop.recyclone.base.api.model.invoke.ResultFilter<?, ?> rf) {
    return copy(rf, null, null);
  }
}
