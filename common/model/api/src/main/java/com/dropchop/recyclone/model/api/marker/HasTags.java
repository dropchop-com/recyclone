package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.tagging.Tag;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
public interface HasTags<T extends Tag<T, TT>, TT extends TitleTranslation> {

  default void addTag(T tag) {
    List<T> tags = this.getTags();
    if (tags == null) {
      tags = new ArrayList<>();
      this.setTags(tags);
    }
    tags.add(tag);
  }

  default <X extends Tag<T, TT>> List<X> getTagsByType(String type) {
    List<X> typedTags = new LinkedList<>();
    for (T tag : getTags()) {
      if (tag == null) {
        continue;
      }
      String typeStr = tag.getType();
      if (typeStr.equals(type)) {
        //noinspection unchecked
        typedTags.add((X)tag);
      }
    }
    return typedTags;
  }

  default <X extends Tag<T, TT>> List<X> getTagsByType(Class<X> tClass) {
    List<X> typedTags = new LinkedList<>();
    for (T tag : getTags()) {
      if (tag == null) {
        continue;
      }
      if (tClass.isInstance(tag)) {
        //noinspection unchecked
        typedTags.add((X)tag);
      }
    }
    return typedTags;
  }

  default <X extends Tag<T, TT>> X getFirstTagByType(String type) {
    for (T tag : getTags()) {
      if (tag == null) {
        continue;
      }
      String typeStr = tag.getType();
      if (typeStr.equals(type)) {
        //noinspection unchecked
        return (X)tag;
      }
    }
    return null;
  }

  default <X extends Tag<T, TT>> X getFirstTagByType(Class<X> tClass) {
    for (T tag : getTags()) {
      if (tag == null) {
        continue;
      }
      if (tClass.isInstance(tag)) {
        //noinspection unchecked
        return (X)tag;
      }
    }
    return null;
  }

  List<T> getTags();
  void setTags(List<T> tags);
}
