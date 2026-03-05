package com.dropchop.recyclone.base.api.model.tagging;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
public interface Tag<T extends Tag<T, TT>, TT extends TitleTranslation> extends Model,
    HasUuid, HasType, HasTranslationInlinedTitle, HasTitleTranslation<TT>, HasAttributes, HasTags<T, TT> {

  default <X extends T> List<X> getFirstTagByType() {
    List<X> typedTags = new LinkedList<>();
    for (T tag : getTags()) {
      if (tag == null) {
        continue;
      }
      if (this.getClass().isInstance(tag)) {
        //noinspection unchecked
        typedTags.add((X)tag);
      }
    }
    return typedTags;
  }
}
