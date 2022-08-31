package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.dropchop.recyclone.model.api.rest.Constants;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 30. 08. 22.
 */
public class FilteringContext {

  private final Deque<String> path = new LinkedList<>();
  final int maxLevel;
  final String contentDetail;
  private int level;
  private Object subject;
  private boolean blocked;

  public FilteringContext(Integer maxLevel, String contentDetail) {
    this.maxLevel = maxLevel == null ? 1 : maxLevel;
    this.contentDetail = contentDetail == null ? Constants.ContentDetail.ALL_OBJS_IDCODE : contentDetail;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void incLevel() {
    this.level++;
  }

  public void decLevel() {
    this.level--;
  }

  public String peek() {
    return path.peekLast();
  }

  public String poll() {
    return path.pollLast();
  }

  public void offer(String name) {
    path.offerLast(name);
  }

  public Object getSubject() {
    return subject;
  }

  public void setSubject(Object subject) {
    this.subject = subject;
  }

  public boolean isBlocked() {
    return blocked;
  }

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }
}
