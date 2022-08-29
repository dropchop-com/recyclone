package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.tagging.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
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

  List<T> getTags();
  void setTags(List<T> tags);
}
