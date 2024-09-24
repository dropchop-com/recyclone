package com.dropchop.recyclone.model.entity.jpa.base;

import com.dropchop.recyclone.model.api.localization.TitleTranslationHelper;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 06. 23.
 */
public interface JpaTitleTranslationHelper extends TitleTranslationHelper<JpaTitleTranslation> {
    @Override
    default JpaTitleTranslation getTranslationInstance(String lngCode) {
        return new JpaTitleTranslation();
    }
}
