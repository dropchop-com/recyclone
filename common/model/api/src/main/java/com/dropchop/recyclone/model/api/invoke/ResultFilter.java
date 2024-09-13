package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 08. 22.
 */
@SuppressWarnings("unused")
public interface ResultFilter<CF extends ResultFilter.ContentFilter, LF extends ResultFilter.LanguageFilter> extends Dto {

  interface ContentFilter extends Model {
    List<String> getIncludes();

    void setIncludes(List<String> includes);

    default ContentFilter includes(List<String> includes) {
      this.setIncludes(includes);
      return this;
    }

    default ContentFilter include(String include) {
      List<String> includes = this.getIncludes();
      if (includes == null) {
        includes = new ArrayList<>();
        this.setIncludes(includes);
      }
      includes.add(include);
      return this;
    }

    List<String> getExcludes();

    void setExcludes(List<String> excludes);

    default ContentFilter excludes(List<String> excludes) {
      this.setExcludes(excludes);
      return this;
    }

    default ContentFilter exclude(String include) {
      List<String> excludes = this.getExcludes();
      if (excludes == null) {
        excludes = new ArrayList<>();
        this.setExcludes(excludes);
      }
      excludes.add(include);
      return this;
    }

    Integer getTreeLevel();

    void setTreeLevel(Integer treeLevel);

    default ContentFilter treeLevel(Integer treeLevel) {
      this.setTreeLevel(treeLevel);
      return this;
    }

    String getDetailLevel();

    void setDetailLevel(String detailLevel);

    default ContentFilter detailLevel(String detailLevel) {
      this.setDetailLevel(detailLevel);
      return this;
    }
  }

  interface LanguageFilter extends Model {
    String getSearch();

    void setSearch(String search);

    default LanguageFilter search(String search) {
      this.setSearch(search);
      return this;
    }

    String getTranslation();

    void setTranslation(String translation);

    default LanguageFilter translation(String translation) {
      this.setTranslation(translation);
      return this;
    }
  }

  static int getSize(Params params, int def) {
    ResultFilter<?,?> filter = params.tryGetResultFilter();
    ResultFilterDefaults defaults = params.tryGetFilterDefaults();
    Integer size = null;
    if (filter != null) {
      size = filter.size();
    } else if (defaults != null) {
      size = defaults.getSize();
    }
    return size == null ? def : size;
  }

  static int getFrom(Params params, int def) {
    ResultFilter<?,?> filter = params.tryGetResultFilter();
    ResultFilterDefaults defaults = params.tryGetFilterDefaults();
    Integer from = null;
    if (filter != null) {
      from = filter.from();
    } else if (defaults != null) {
      from = defaults.getFrom();
    }
    return from == null ? def : from;
  }

  CF getContent();
  void setContent(CF contentFilter);

  default CF content() {
    return getContent();
  }

  default ResultFilter<CF, LF> content(CF contentFilter) {
    setContent(contentFilter);
    return this;
  }

  LF getLang();
  void setLang(LF languageFilter);

  default LF lang() {
    return getLang();
  }

  default ResultFilter<CF, LF> lang(LF languageFilter) {
    setLang(languageFilter);
    return this;
  }

  String getVersion();
  void setVersion(String version);

  default String version() {
    return getVersion();
  }

  default ResultFilter<CF, LF> version(String version) {
    setVersion(version);
    return this;
  }

  int getFrom();
  void setFrom(int from);

  default int from() {
    return getFrom();
  }

  default ResultFilter<CF, LF> from(int from) {
    setFrom(from);
    return this;
  }

  int getSize();
  void setSize(int size);

  default int size() {
    return getSize();
  }

  default ResultFilter<CF, LF> size(int size) {
    setSize(size);
    return this;
  }

  List<String> getStates();
  void setStates(List<String> states);

  default List<String> states() {
    return getStates();
  }

  default ResultFilter<CF, LF> states(List<String> states) {
    setStates(states);
    return this;
  }

  default ResultFilter<CF, LF> state(String state) {
    List<String> states = getStates();
    if (states == null) {
      states = new ArrayList<>();
      setStates(states);
    }
    states.add(state);
    return this;
  }

  List<String> getSort();
  void setSort(List<String> sort);

  default List<String> sort() {
    return getStates();
  }

  default ResultFilter<CF, LF> sort(List<String> sort) {
    setSort(sort);
    return this;
  }

  default ResultFilter<CF, LF> sort(String sort) {
    List<String> sorts = getSort();
    if (sorts == null) {
      sorts = new ArrayList<>();
      setSort(sorts);
    }
    sorts.add(sort);
    return this;
  }
}
