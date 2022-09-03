package com.dropchop.recyclone.model.dto.filtering;

import com.dropchop.recyclone.model.api.utils.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
@Slf4j
public class MarkerFilterSegment extends FilterSegment {

  public static MarkerFilterSegment parse(String pattern, Integer maxLevel, Class<?> ... markers) {
    if (pattern == null || pattern.isBlank()) {
      return null;
    }
    if (pattern.startsWith(PATH_DELIM)) {
      pattern = pattern.substring(1);
    }
    String[] pathStr = pattern.split("\\" + PATH_DELIM, 255);
    return new MarkerFilterSegment(pathStr, maxLevel, List.of(markers));
  }

  public final Collection<Class<?>> markers;

  public MarkerFilterSegment(String[] path, Integer maxLevel, Collection<Class<?>> markers) {
    super(path, maxLevel);
    this.markers = markers;
  }

  protected boolean testInstance(PathSegment segment) {
    if (segment.referer == null) {
      return false;
    }
    Class<?> clazz = segment.referer.getClass();
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
