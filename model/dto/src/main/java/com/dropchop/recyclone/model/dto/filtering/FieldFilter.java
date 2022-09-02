package com.dropchop.recyclone.model.dto.filtering;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ResultFilter.ContentFilter;
import com.dropchop.recyclone.model.api.invoke.ResultFilterDefaults;
import com.dropchop.recyclone.model.api.localization.Translation;
import com.dropchop.recyclone.model.api.marker.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static com.dropchop.recyclone.model.api.rest.Constants.ContentDetail.NESTED_PREFIX;
import static com.dropchop.recyclone.model.dto.filtering.FilterSegment.any;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
public class FieldFilter implements Predicate<PathSegment> {

  final LinkedList<FilterSegment> includes;
  final LinkedList<FilterSegment> excludes;

  public static FieldFilter fromFilter(ContentFilter contentFilter, ResultFilterDefaults filterDefaults) {
    Integer treeLevel = null;
    String detailLevel = null;
    List<String> includesStrings = new ArrayList<>();
    List<String> excludesStrings = new ArrayList<>();
    if (filterDefaults != null) {
      detailLevel = filterDefaults.getDetailLevel();
      treeLevel = filterDefaults.getTreeLevel();
    }
    if (contentFilter != null) {
      Integer contentTreeLevel = contentFilter.getTreeLevel();
      if (contentTreeLevel != null) {
        treeLevel = contentTreeLevel;
        detailLevel = null;
      }
      String contentDetailLevel = contentFilter.getDetailLevel();
      if (contentDetailLevel != null && !contentDetailLevel.isBlank()) {
        detailLevel = contentDetailLevel;
      }

      List<String> tmp = contentFilter.getIncludes();
      if (tmp != null) {
        includesStrings = tmp;
      }
      tmp = contentFilter.getExcludes();
      if (tmp != null) {
        excludesStrings = tmp;
      }
    }

    LinkedList<FilterSegment> includes = new LinkedList<>();
    for (String includeStr : includesStrings) {
      FilterSegment include = FilterSegment.parse(includeStr, treeLevel);
      if (include != null) {
        includes.add(include);
      }
    }
    LinkedList<FilterSegment> excludes = new LinkedList<>();
    for (String excludeStr : excludesStrings) {
      FilterSegment exclude = FilterSegment.parse(excludeStr, treeLevel);
      if (exclude != null) {
        excludes.add(exclude);
      }
    }

    List<String> availableDetails = filterDefaults.getAvailableLevelOfContentDetails();
    if (detailLevel != null && availableDetails != null && availableDetails.contains(detailLevel)) {
      int tmpLevel = treeLevel == null ? 1 : treeLevel;
      if (detailLevel.startsWith(NESTED_PREFIX)) {
        tmpLevel++;
      }
      FilterSegment anyParent = any(tmpLevel);

      includes.add(new MarkerFilterSegment(anyParent, "id", tmpLevel, HasId.class));
      includes.add(new MarkerFilterSegment(anyParent, "id", tmpLevel, HasUuid.class));
      includes.add(new MarkerFilterSegment(anyParent, "code", tmpLevel, HasCode.class));
      if (detailLevel.contains("_title")) {
        includes.add(new MarkerFilterSegment(anyParent, "title", tmpLevel, HasTitle.class));
        includes.add(new MarkerFilterSegment(anyParent, "name", tmpLevel, HasName.class));
        includes.add(new MarkerFilterSegment(anyParent, "lang", tmpLevel,
          HasTranslation.class, HasTranslationInlined.class));
      }
      if (detailLevel.contains("_trans")) {
        includes.add(new MarkerFilterSegment(anyParent, "translations", tmpLevel, HasTranslation.class));
        includes.add(new MarkerFilterSegment(anyParent, "translations[*]", tmpLevel, Translation.class));
      }
    } else if (includes.isEmpty() && excludes.isEmpty()) {
      includes.add(any(treeLevel));
    }
    return new FieldFilter(includes, excludes);
  }

  public static FieldFilter fromParams(Params params) {
    ResultFilterDefaults filterDefaults = params.tryGetFilterDefaults();
    ContentFilter contentFilter = params.tryGetResultContentFilter();
    return fromFilter(contentFilter, filterDefaults);
  }

  FieldFilter(LinkedList<FilterSegment> includes, LinkedList<FilterSegment> excludes) {
    this.includes = includes;
    this.excludes = excludes;
  }

  @Override
  public boolean test(PathSegment path) {
    if (path.parent == null) {// we always accept root
      return true;
    }
    boolean incl = false;
    for (FilterSegment filter : includes) {
      if (filter.test(path)) {
        incl = true;
        break;
      }
    }
    if (incl) {
      for (FilterSegment filter : excludes) {
        if (filter.test(path)) {
          incl = false;
          break;
        }
      }
    }
    return incl;
  }

  public boolean dive(PathSegment path) {
    if (path.parent == null) {// we always accept root
      return true;
    }
    boolean incl = false;
    for (FilterSegment filter : includes) {
      if (filter.dive(path)) {
        incl = true;
        break;
      }
    }
    if (incl) {
      for (FilterSegment filter : excludes) {
        if (filter.test(path)) {
          incl = false;
          break;
        }
      }
    }
    return incl;
  }
}
