package com.dropchop.recyclone.model.entity.jpa.base;

import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslationHelper;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 06. 23.
 */
public interface JpaTitleDescriptionTranslationHelper extends TitleDescriptionTranslationHelper<JpaTitleDescriptionTranslation> {
    @Override
    default JpaTitleDescriptionTranslation getTranslationInstance(String lngCode) {
        return new JpaTitleDescriptionTranslation();
    }
}
