package com.dropchop.recyclone.base.api.model.filtering;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.invoke.CommonParams;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter.ContentFilter;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilterDefaults;
import com.dropchop.recyclone.base.api.model.localization.Translation;
import com.dropchop.recyclone.base.api.model.marker.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static com.dropchop.recyclone.base.api.model.filtering.FilterSegment.any;
import static com.dropchop.recyclone.base.api.model.rest.Constants.ContentDetail.*;

/**
 * FiledFilter object that can evaluate include / exclude FilterSegment rules against PathSegment.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 09. 22.
 */
public class FieldFilter implements Predicate<PathSegment> {

  private static void addCommonIncludes(LinkedList<FilterSegment> includes, String detailLevel, int tmpLevel) {
    includes.add(MarkerFilterSegment.parse("*.id", tmpLevel, HasId.class));
    includes.add(MarkerFilterSegment.parse("*.id", tmpLevel, HasUuid.class));
    includes.add(MarkerFilterSegment.parse("*.code", tmpLevel, HasCode.class));
    includes.add(MarkerFilterSegment.parse("*.type", tmpLevel, HasType.class));
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

  /**
   * FileFilter is constructed from ResultFilterDefaults and ResultFilter objects contained in Parameters.
   * Construction examines ContentDetail constant, tree depth and include / exclude fields,
   * and then decides which include / exclude FilterSegments to construct and include.
   *
   * @param contentFilter content filter to collect construct parameters from.
   * @param filterDefaults defaults to use when content filter parameter is missing.
   * @return FieldFilter.
   */

  public static FieldFilter fromFilter(ContentFilter contentFilter, ResultFilterDefaults filterDefaults) {
    Integer treeLevel = null;
    String detailLevel = null;
    List<String> includesStrings = new ArrayList<>();
    List<String> excludesStrings = new ArrayList<>();

    if (contentFilter != null) {
      Integer contentTreeLevel = contentFilter.getTreeLevel();
      if (contentTreeLevel != null) {
        treeLevel = contentTreeLevel;
        //detailLevel = null;
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

    if (filterDefaults != null) {
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
    }

    if (treeLevel != null && includes.isEmpty()) {
      includes.add(any(treeLevel));
    }
    return new FieldFilter(treeLevel != null ? treeLevel : Integer.MAX_VALUE, detailLevel, includes, excludes);
  }

  /**
   * If params are of type CommonParams thn include / exclude FilterSegments are
   * constructed from ResultFilterDefaults and ResultFilter objects contained in Parameters.
   *
   * @see FieldFilter#fromFilter(ContentFilter, ResultFilterDefaults)
   *
   * @param params parameters to construct field filter from.
   * @return field filter or null if params is not of type CommonParams
   */
  public static FieldFilter fromParams(Params params) {
    if (!(params instanceof CommonParams)) {
      return null;
    }
    ResultFilterDefaults filterDefaults = params.tryGetFilterDefaults();
    ContentFilter contentFilter = params.tryGetResultContentFilter();
    return fromFilter(contentFilter, filterDefaults);
  }


  final LinkedList<FilterSegment> includes;
  final LinkedList<FilterSegment> excludes;

  private final int treeLevel;
  private final boolean cdNested;
  private final boolean cdTrans;

  FieldFilter(int treeLevel, String detailLevel, LinkedList<FilterSegment> includes, LinkedList<FilterSegment> excludes) {
    this.includes = includes;
    this.excludes = excludes;
    this.treeLevel = treeLevel;
    this.cdNested = detailLevel != null && detailLevel.startsWith(NESTED_PREFIX);
    this.cdTrans = detailLevel != null && detailLevel.contains(TRANS_SUFIX);
  }

  private boolean specialCollectionDive(PathSegment segment) {
    if (treeLevel == Integer.MAX_VALUE) {
      return true;
    }
    if (segment == null || segment.referer == null) {
      return true;
    }
    if (!MarkerFilterSegment.isSpecialCollection(segment)) {
      return cdNested ? segment.level < treeLevel + 1: segment.level < treeLevel;
    }

    if (cdNested) {
      if (cdTrans) {
        return segment.level <= treeLevel + 1;
      } else {
        return true;
      }
    } else {
      if (cdTrans) {
        return segment.level <= treeLevel;
      } else {
        return true;
      }
    }
  }

  /**
   * Determine if given path segment is excluded by any exclude filter segment @see: FilterSegment.
   *
   * @param segment current path segment to examine
   * @return true if any exclude FilterSegment passes test false otherwise.
   */
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

  /**
   * Determine if given path segment passes any include filter segments @see: FilterSegment.
   *
   * @param segment current path segment to examine
   * @return true if any include FilterSegment passes test false otherwise.
   */
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

  /**
   * Determine if given path segment passes all include / exclude filter segments @see: FilterSegment.
   *
   * @param segment current path segment to examine
   * @return true if continue depth processing false otherwise.
   */
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


  /**
   * Determine if we should continue processing and dive into object for given path segment
   *
   * @param segment current path segment to examine
   * @return true if continue depth processing false otherwise.
   */
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

  /**
   * Predetermine if we should continue processing and dive into object for given property.
   * This evaluation is more restrictive. Property must pass @see: nest() and @see dive(),
   * be a special collection, right level and content detail must match.
   * This method helps to block needles child collection examination,
   * which is helpful with ORM object filtering.
   *
   * @param segment objects property path segment.
   * @return true if continue depth processing false otherwise.
   */
  public boolean propertyDive(PathSegment segment) {
    if (segment.parent == null) {// we always accept root
      return true;
    }
    boolean nestAndDive = false;
    for (FilterSegment filter : includes) {
      if (filter.nest(segment)) {
        if (filter.dive(segment, true)) {
          nestAndDive = true;
          break;
        }
      }
    }
    if (nestAndDive) {
      nestAndDive = !exclude(segment);
    }
    return nestAndDive && specialCollectionDive(segment);
  }
}
