package com.dropchop.recyclone.service.api.invoke;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 5. 05. 22.
 */
class FieldFilterOldTest {

  @Test
  void parseFilterSegments() {
    String pattStr = ".actions.code;title;lang";
    FieldFilterOld filter = new FieldFilterOld();
    filter.parseFilterSegments(pattStr);
    assertEquals(filter.path.get(0).name, ".");
    assertEquals(filter.path.get(1).name, "actions");
    assertEquals(filter.path.get(2).name, "code");
    assertEquals(filter.path.get(2).siblings, List.of("title","lang"));


    pattStr = ".actions.code;title;lang.*";
    filter.parseFilterSegments(pattStr);
    assertEquals(filter.path.get(0).name, ".");
    assertEquals(filter.path.get(1).name, "actions");
    assertEquals(filter.path.get(2).name, "code");
    assertEquals(filter.path.get(2).siblings, List.of("title","lang"));
    assertEquals(filter.path.get(3).name, "*");

    pattStr = "actions.code;title;lang.*";
    filter.parseFilterSegments(pattStr);
    assertEquals(filter.path.get(0).name, ".");
    assertEquals(filter.path.get(1).name, "actions");
    assertEquals(filter.path.get(2).name, "code");
    assertEquals(filter.path.get(2).siblings, List.of("title","lang"));
    assertEquals(filter.path.get(3).name, "*");
  }

  @Test
  void matchesSimple() {
    String pattStr = "actions.nested1;nested2;nested3.*";
    Deque<FieldFilterOld.PathSegment> deque = new LinkedList<>();
    deque.offerLast(new FieldFilterOld.PathSegment(FieldFilterOld.ROOT_OBJECT));
    deque.offerLast(new FieldFilterOld.PathSegment("actions"));
    deque.offerLast(new FieldFilterOld.PathSegment("nested1"));
    assertTrue(new FieldFilterOld().parseFilterSegments(pattStr).matches(deque, "translation"));

    deque = new LinkedList<>();
    deque.offerLast(new FieldFilterOld.PathSegment(FieldFilterOld.ROOT_OBJECT));
    deque.offerLast(new FieldFilterOld.PathSegment("actions"));
    deque.offerLast(new FieldFilterOld.PathSegment("nested1"));
    deque.offerLast(new FieldFilterOld.PathSegment("anything"));
    assertFalse(new FieldFilterOld().parseFilterSegments(pattStr).matches(deque, "translation"));
  }
}