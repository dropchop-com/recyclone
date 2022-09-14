package com.dropchop.recyclone.model.api.filtering;

import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.api.marker.HasTranslation;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

import static com.dropchop.recyclone.model.api.rest.Constants.ContentDetail.NESTED_PREFIX;
import static com.dropchop.recyclone.model.api.rest.Constants.ContentDetail.TRANS_SUFIX;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
@Slf4j
public class MarkerFilterSegment extends FilterSegment {

  public static MarkerFilterSegment parse(String pattern, Integer maxLevel,
                                          boolean testParentInstance,
                                          Class<?> ... markers) {
    if (pattern == null || pattern.isBlank()) {
      return null;
    }
    if (pattern.startsWith(PATH_DELIM)) {
      pattern = pattern.substring(1);
    }
    String[] pathStr = pattern.split("\\" + PATH_DELIM, 255);
    return new MarkerFilterSegment(pathStr, maxLevel, testParentInstance, List.of(markers));
  }

  public static MarkerFilterSegment parse(String pattern, Integer maxLevel,
                                          Class<?> ... markers) {
    return parse(pattern, maxLevel, false, markers);
  }

  public final Collection<Class<?>> markers;
  public final boolean testParentInstance;



  public MarkerFilterSegment(String[] path, Integer maxLevel,
                             boolean testParentInstance,
                             Collection<Class<?>> markers) {
    super(path, maxLevel);
    this.markers = markers;
    this.testParentInstance = testParentInstance;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean isSpecialCollection(PathSegment segment) {
    Class<?> currentClass = segment.referer.getClass();
    String propName = segment.name;
    boolean isTranslations = HasTranslation.class.isAssignableFrom(currentClass) && "translations".equals(propName);
    boolean isAttributes = HasAttributes.class.isAssignableFrom(currentClass) && "attributes".equals(propName);
    if (!(isTranslations || isAttributes)) {
      return false;
    }
    return segment.collectionLike;
  }

  protected boolean testInstance(PathSegment segment) {
    Class<?> clazz;
    if (testParentInstance) {
      PathSegment parent = segment.parent;
      clazz = null;
      while (parent != null) {
        if (!(parent instanceof CollectionPathSegment)) {
          if (parent.referer == null) {
            break;
          }
          clazz = parent.referer.getClass();
          break;
        }
        parent = parent.parent;
      }
      if (clazz == null) {
        return false;
      }
    } else {
      if (segment.referer == null) {
        return false;
      }
      clazz = segment.referer.getClass();
    }
    for (Class<?> marker : markers) {
      if (!marker.isAssignableFrom(clazz)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean test(PathSegment segment) {
    if (!testLevel(segment)) {
      return false;
    }
    if (!testInstance(segment)) {
      return false;
    }
    //log.info("Test passes in [{}][{}]", segment, result);
    return testName(segment);
  }
}
