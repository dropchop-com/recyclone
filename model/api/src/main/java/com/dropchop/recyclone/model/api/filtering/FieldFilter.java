package com.dropchop.recyclone.model.api.filtering;

import com.dropchop.recyclone.model.api.attr.Attribute;
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

import static com.dropchop.recyclone.model.api.filtering.FilterSegment.any;
import static com.dropchop.recyclone.model.api.rest.Constants.ContentDetail.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
public class FieldFilter implements Predicate<PathSegment> {

  final LinkedList<FilterSegment> includes;
  final LinkedList<FilterSegment> excludes;

  private final int treeLevel;
  private final String detailLevel;

  private static void addCommonIncludes(LinkedList<FilterSegment> includes, String detailLevel, int tmpLevel) {
    includes.add(MarkerFilterSegment.parse("*.id", tmpLevel, HasId.class));
    includes.add(MarkerFilterSegment.parse("*.id", tmpLevel, HasUuid.class));
    includes.add(MarkerFilterSegment.parse("*.code", tmpLevel, HasCode.class));
    if (detailLevel.contains(TITLE_SUFIX) || detailLevel.contains(TRANS_SUFIX)) {
      includes.add(MarkerFilterSegment.parse("*.title", tmpLevel, HasTitle.class));
      includes.add(MarkerFilterSegment.parse("*.name", tmpLevel, HasName.class));
      includes.add(MarkerFilterSegment.parse("*.lang", tmpLevel,
        HasTranslation.class, HasTranslationInlined.class));
    }
    if (detailLevel.contains(TRANS_SUFIX)) {
      includes.add(MarkerFilterSegment.parse("*.translations", tmpLevel + 1, true, HasTranslation.class));
      includes.add(MarkerFilterSegment.parse("*.translations[*].*", tmpLevel + 2, Translation.class));
      includes.add(MarkerFilterSegment.parse("*.attributes", tmpLevel + 1, HasAttributes.class));
      includes.add(MarkerFilterSegment.parse("*.attributes[*].*", tmpLevel + 2, Attribute.class));
    }
  }

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

        //include special collections of current object by convention
        includes.add(MarkerFilterSegment.parse("*.translations", origLevel, HasTranslation.class));
        includes.add(MarkerFilterSegment.parse("*.translations[*].*", origLevel + 1, Translation.class));
        includes.add(MarkerFilterSegment.parse("*.attributes", origLevel, HasAttributes.class));
        includes.add(MarkerFilterSegment.parse("*.attributes[*].*", origLevel + 1, Attribute.class));

        //include nested objects fields
        addCommonIncludes(includes, detailLevel, tmpLevel);
      } else {
        addCommonIncludes(includes, detailLevel, tmpLevel);
        if (!detailLevel.contains(TRANS_SUFIX)) {
          // we exclude translations
          excludes.add(MarkerFilterSegment.parse("*.translations", tmpLevel, true, HasTranslation.class));
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
      return true;
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
    boolean incl = include(segment);
    if (incl) {
      incl = !exclude(segment);
    }
    return incl;
  }

  public boolean nest(PathSegment segment) {
    if (segment.parent == null) {// we always accept root
      return true;
    }
    boolean nest = false;
    for (FilterSegment filter : includes) {
      if (filter.nest(segment)) {
        nest = true;
        break;
      }
    }
    if (nest) {
      nest = !exclude(segment);
    }
    return nest;
  }

  public boolean dive(PathSegment segment) {
    if (segment.parent == null) {// we always accept root
      return true;
    }
    boolean dive = false;
    for (FilterSegment filter : includes) {
      if (filter.dive(segment)) {
        dive = true;
        break;
      }
    }
    if (dive) {
      dive = !exclude(segment);
    }
    return dive;
  }
}
