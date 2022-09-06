package com.dropchop.recyclone.model.dto.filtering;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 09. 22.
 */
@SuppressWarnings("UnusedReturnValue")
public class FilteringState {
  private final Deque<Boolean> starts = new LinkedList<>();
  private final Deque<String> fields = new LinkedList<>();
  private final Deque<PathSegment> segments = new LinkedList<>();
  private final Deque<Object> objects = new LinkedList<>();

  public void pushSegment(PathSegment segment) {
    segments.offerLast(segment);
  }

  public PathSegment pollSegment() {
    return segments.pollLast();
  }

  public PathSegment currentSegment() {
    return segments.peekLast();
  }

  public void pushObject(Object object) {
    objects.offerLast(object);
  }

  public Object pollObject() {
    return objects.pollLast();
  }

  public Object currentObject() {
    return objects.peekLast();
  }

  public void pushField(String field) {
    fields.offerLast(field);
  }

  public String pollField() {
    return fields.pollLast();
  }

  public void divePassed() {
    starts.offerLast(Boolean.TRUE);
  }

  public void diveSkipped() {
    starts.offerLast(Boolean.FALSE);
  }

  public Boolean dived() {
    return starts.pollLast();
  }
}
