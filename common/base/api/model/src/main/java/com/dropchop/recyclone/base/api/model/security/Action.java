package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasTitleDescriptionTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasTranslationInlinedTitleDescription;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 01. 22.
 */
public interface Action<TDT extends TitleDescriptionTranslation>
  extends Model, HasCode, HasTranslationInlinedTitleDescription, HasTitleDescriptionTranslation<TDT> {
}
