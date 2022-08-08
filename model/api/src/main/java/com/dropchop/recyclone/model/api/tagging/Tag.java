package com.dropchop.recyclone.model.api.tagging;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
public interface Tag<T extends Tag<T, TT>, TT extends TitleTranslation> extends Model,
  HasUuid, HasType, HasTranslationInlinedTitle, HasTitleTranslation<TT>, HasAttributes, HasTags<T, TT> {
}
