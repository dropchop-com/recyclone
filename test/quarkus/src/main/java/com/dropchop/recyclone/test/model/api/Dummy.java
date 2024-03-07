package com.dropchop.recyclone.test.model.api;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasTitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.HasTranslationInlinedTitleDescription;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 03. 24.
 */
public interface Dummy <TDT extends TitleDescriptionTranslation>
    extends Model, HasCode, HasTranslationInlinedTitleDescription, HasTitleDescriptionTranslation<TDT> {
}
