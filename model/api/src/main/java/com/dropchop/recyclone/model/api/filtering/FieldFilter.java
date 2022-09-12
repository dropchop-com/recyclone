package com.dropchop.recyclone.model.api.filtering;

import com.dropchop.recyclone.model.api.invoke.CommonParams;
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
import static com.dropchop.recyclone.model.api.filtering.FilterSegment.any;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
public class FieldFilter implements Predicate<PathSegment> {

  final LinkedList<FilterSegment> includes;
  final LinkedList<FilterSegment> excludes;

  private int treeLevel;
  private String detailLevel = null;

  public static FieldFilter fromFilter(ContentFilter contentFilter, ResultFilterDefaults filterDefaults) {
    Integer treeLevel = null;
    String detailLevel = null;
    List<String> includesStrings = new ArrayList<>();
    List<String> excludesStrings = new ArrayList<>();

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

    if (detailLevel == null && treeLevel == null && includesStrings.isEmpty() && filterDefaults != null) {
      detailLevel = filterDefaults.getDetailLevel();
    }

    if (treeLevel == null && filterDefaults != null) {
      treeLevel = filterDefaults.getTreeLevel();
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
      int origLevel = treeLevel == null ? 1 : treeLevel;
      int tmpLevel = origLevel;
      if (detailLevel.startsWith(NESTED_PREFIX)) {
        includes.add(any(origLevel));
        tmpLevel++;

        includes.add(MarkerFilterSegment.parse("*.translations", origLevel, HasTranslation.class));
        includes.add(MarkerFilterSegment.parse("*.translations[*].*", origLevel + 1, Translation.class));

        includes.add(MarkerFilterSegment.parse("*.id", tmpLevel, HasId.class));
        includes.add(MarkerFilterSegment.parse("*.id", tmpLevel, HasUuid.class));
        includes.add(MarkerFilterSegment.parse("*.code", tmpLevel, HasCode.class));
        if (detailLevel.contains("_title") || detailLevel.contains("_trans")) {
          includes.add(MarkerFilterSegment.parse("*.title", tmpLevel, HasTitle.class));
          includes.add(MarkerFilterSegment.parse("*.name", tmpLevel, HasName.class));
          includes.add(MarkerFilterSegment.parse("*.lang", tmpLevel,
            HasTranslation.class, HasTranslationInlined.class));
        }
        if (detailLevel.contains("_trans")) {
          includes.add(MarkerFilterSegment.parse("*.translations", tmpLevel + 1, true, HasTranslation.class));
          includes.add(MarkerFilterSegment.parse("*.translations[*].*", tmpLevel + 2, Translation.class));
        }
      } else {
        includes.add(MarkerFilterSegment.parse("*.id", tmpLevel, HasId.class));
        includes.add(MarkerFilterSegment.parse("*.id", tmpLevel, HasUuid.class));
        includes.add(MarkerFilterSegment.parse("*.code", tmpLevel, HasCode.class));
        if (detailLevel.contains("_title") || detailLevel.contains("_trans")) {
          includes.add(MarkerFilterSegment.parse("*.title", tmpLevel, HasTitle.class));
          includes.add(MarkerFilterSegment.parse("*.name", tmpLevel, HasName.class));
          includes.add(MarkerFilterSegment.parse("*.lang", tmpLevel,
            HasTranslation.class, HasTranslationInlined.class));
        }
        if (detailLevel.contains("_trans")) {
          includes.add(MarkerFilterSegment.parse("*.translations", tmpLevel + 1, true, HasTranslation.class));
          includes.add(MarkerFilterSegment.parse("*.translations[*].*", tmpLevel + 2, Translation.class));
        } else {
          excludes.add(MarkerFilterSegment.parse("*.translations", tmpLevel, true, HasTranslation.class));
          //excludes.add(MarkerFilterSegment.parse("*.translations[*].*", tmpLevel + 2, Translation.class));
        }
      }
    }

    if (treeLevel != null && includes.isEmpty()) {
      includes.add(any(treeLevel));
    }
    return new FieldFilter(treeLevel != null ? treeLevel : Integer.MAX_VALUE, detailLevel, includes, excludes);
  }

  public static FieldFilter fromParams(Params params) {
    if (!(params instanceof CommonParams)) {
      return null;
    }
    ResultFilterDefaults filterDefaults = params.tryGetFilterDefaults();
    ContentFilter contentFilter = params.tryGetResultContentFilter();
    return fromFilter(contentFilter, filterDefaults);
  }

  FieldFilter(int treeLevel, String detailLevel, LinkedList<FilterSegment> includes, LinkedList<FilterSegment> excludes) {
    this.includes = includes;
    this.excludes = excludes;
    this.treeLevel = treeLevel;
    this.detailLevel = detailLevel;
  }

  public int getTreeLevel() {
    return treeLevel;
  }

  public String getDetailLevel() {
    return detailLevel;
  }

  public boolean exclude(PathSegment segment) {
    if (segment.parent == null) {// we always accept root
      return false;
    }
    for (FilterSegment filter : excludes) {
      if (filter.test(segment)) {
        return true;
      }
    }
    return false;
  }

  public boolean include(PathSegment segment) {
    if (segment.parent == null) {// we always accept root
      return false;
    }
    for (FilterSegment filter : includes) {
      if (filter.test(segment)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean test(PathSegment segment) {
    if (segment.parent == null) {// we always accept root
      return true;
    }
    boolean incl = false;
    for (FilterSegment filter : includes) {
      if (filter.test(segment)) {
        incl = true;
        break;
      }
    }
    if (incl) {
      for (FilterSegment filter : excludes) {
        if (filter.test(segment)) {
          incl = false;
          break;
        }
      }
    }
    return incl;
  }

  public boolean nest(PathSegment segment) {
    if (segment.parent == null) {// we always accept root
      return true;
    }
    boolean incl = false;
    for (FilterSegment filter : includes) {
      if (filter.nest(segment)) {
        incl = true;
        break;
      }
    }
    if (incl) {
      for (FilterSegment filter : excludes) {
        if (filter.test(segment)) {
          incl = false;
          break;
        }
      }
    }
    return incl;
  }

  public boolean dive(PathSegment segment) {
    if (segment.parent == null) {// we always accept root
      return true;
    }
    boolean incl = false;
    for (FilterSegment filter : includes) {
      if (filter.dive(segment)) {
        incl = true;
        break;
      }
    }
    if (incl) {
      for (FilterSegment filter : excludes) {
        if (filter.test(segment)) {
          incl = false;
          break;
        }
      }
    }
    return incl;
  }
}
