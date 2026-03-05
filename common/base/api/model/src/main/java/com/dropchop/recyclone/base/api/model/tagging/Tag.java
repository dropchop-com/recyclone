package com.dropchop.recyclone.base.api.model.tagging;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.*;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@SuppressWarnings("unused")
public interface Tag<T extends Tag<T, TT>, TT extends TitleTranslation> extends Model,
    HasUuid, HasType, HasTranslationInlinedTitle, HasTitleTranslation<TT>, HasAttributes, HasTags<T, TT> {

  default <X extends T> List<X> findTagsByType() {
    //noinspection unchecked
    return (List<X>) this.getTagsByType(this.getClass());
  }

  default <X extends T> X findFirstTagByType() {
    //noinspection unchecked
    return (X) this.getFirstTagByType(this.getClass());
  }
}
