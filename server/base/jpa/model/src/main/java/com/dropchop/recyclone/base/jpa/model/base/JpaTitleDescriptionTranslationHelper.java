package com.dropchop.recyclone.base.jpa.model.base;

import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslationHelper;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 06. 23.
 */
public interface JpaTitleDescriptionTranslationHelper extends TitleDescriptionTranslationHelper<JpaTitleDescriptionTranslation> {
    @Override
    default JpaTitleDescriptionTranslation getTranslationInstance(String lngCode) {
        return new JpaTitleDescriptionTranslation();
    }
}
