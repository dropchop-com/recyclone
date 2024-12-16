package com.dropchop.recyclone.base.api.model.localization;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasTranslationInlinedTitle;
import com.dropchop.recyclone.base.api.model.marker.HasTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
public interface Country<TT extends TitleTranslation>
  extends Model, HasCode, HasTranslationInlinedTitle, HasTitleTranslation<TT> {
}
