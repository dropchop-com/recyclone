package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasTranslationInlinedTitle;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
public interface Country<TT extends TitleTranslation>
  extends Model, HasCode, HasTranslationInlinedTitle, HasTitleTranslation<TT> {
}
