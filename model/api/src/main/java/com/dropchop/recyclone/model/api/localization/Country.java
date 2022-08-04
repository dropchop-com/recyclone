package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasEmbeddedTitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
public interface Country<TT extends TitleTranslation>
  extends Model, HasCode, HasEmbeddedTitleTranslation, HasTitleTranslation<TT> {
}
