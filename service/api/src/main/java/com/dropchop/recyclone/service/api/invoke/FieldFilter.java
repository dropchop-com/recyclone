package com.dropchop.recyclone.service.api.invoke;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * FieldFilter is a container object for current graph path.
 * It contains path in a form of path segments list.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 5. 05. 22.
 */
class FieldFilter {

  public static final String ROOT_OBJECT = ".";
  public static final String PATH_DELIM = ".";
  public static final String SIBLING_DELIM = ";";
  public static final String ANY = "*";

  static class PathSegment {
    final String name;
    Object referer;
    int index = -1;
    int level = 0;
    String path = null;

    public PathSegment(String name, int index, Object referer) {
      this.name = name;
      this.index = index;
      this.referer = referer;
    }

    public PathSegment(String name, Object referer) {
      this(name, -1, referer);
    }

    public PathSegment(String name) {
      this.name = name;
    }

    boolean isCollectionElement() {
      return this.index > -1;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  static class CollectionPathSegment extends PathSegment {
    int currIndex;

    public CollectionPathSegment(String name, int index, Object referer) {
      super(name, index, referer);
    }

    public CollectionPathSegment(String name, Object referer) {
      super(name, referer);
    }
  }

  static class FilterPathSegment extends PathSegment {
    final List<String> siblings = new ArrayList<>();
    final boolean isWildcard;

    public FilterPathSegment(String name) {
      super(name);
      isWildcard = name.equals(ANY);
    }

    public void addSibling(@NonNull String sibling) {
      siblings.add(sibling);
    }
  }

  final LinkedList<FilterPathSegment> path = new LinkedList<>();


  static PathSegment computePath(@NonNull Deque<PathSegment> path, @NonNull String lastProp) {
    StringBuilder strPath = new StringBuilder();

    PathSegment last = new PathSegment(lastProp, -1, path.getLast().referer);
    path.offerLast(last);

    int level = 0;
    for (PathSegment segment : path) {
      segment.level = level;
      if (segment instanceof CollectionPathSegment) {
        if (strPath.length() > 0 && level > 1) {
          segment.path = strPath + PATH_DELIM + segment.name;
        } else {
          segment.path = strPath + segment.name;
        }
        continue;
      }
      segment.path = strPath.toString();
      if (strPath.length() > 0 && level > 1) {
        strPath.append(PATH_DELIM);
      }
      strPath.append(segment.name);
      if (segment.isCollectionElement()) {
        strPath.append("[");
        strPath.append(segment.index);
        strPath.append("]");
      }
      segment.path = strPath.toString();
      level++;
    }

    return path.pollLast();
  }


  FieldFilter parseFilterSegments(@NonNull String filterPatternStr) {
    if (filterPatternStr.startsWith(ROOT_OBJECT)) {
      filterPatternStr = filterPatternStr.substring(1);
    }
    if (!path.isEmpty()) {
      path.clear();
    }

    String[] parts = filterPatternStr.split("\\" + PATH_DELIM);
    this.path.offerLast(new FilterPathSegment(ROOT_OBJECT));
    for (String part : parts) {
      if (part.contains(SIBLING_DELIM)) {
        String[] subParts = part.split("\\" + SIBLING_DELIM, -1);
        FilterPathSegment segment = new FilterPathSegment(subParts[0]);
        for (int i = 1; i < subParts.length; i++) {
          if (subParts[i] == null || subParts[i].isBlank()) {
            continue;
          }
          segment.addSibling(subParts[i]);
        }
        this.path.offerLast(segment);
      } else {
        this.path.offerLast(new FilterPathSegment(part));
      }
    }

    return this;
  }

  boolean matches(Deque<PathSegment> path, String lastProp) {
    int level = 0;
    LinkedList<PathSegment> pathCopy = new LinkedList<>(path);

    PathSegment last = new PathSegment(lastProp, -1, pathCopy.getLast().referer);
    pathCopy.offerLast(last);

    for (PathSegment segment : pathCopy) {
      if (segment instanceof CollectionPathSegment) {
        continue;
      }
      if (this.path.size() <= level) {
        return false;
      }
      FilterPathSegment filterSegment = this.path.get(level);
      if (!filterSegment.isWildcard) {
        boolean matched = filterSegment.name.equals(segment.name);
        if (!matched) {
          for (String sibling : filterSegment.siblings) {
            matched = sibling.equals(segment.name);
            if (matched) {
              break;
            }
          }
        }
        if (!matched) {
          return false;
        }
      }
      level++;
    }

    return true;
  }
}
