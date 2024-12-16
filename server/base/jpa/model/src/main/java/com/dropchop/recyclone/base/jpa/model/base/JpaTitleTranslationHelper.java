package com.dropchop.recyclone.base.jpa.model.base;

import com.dropchop.recyclone.base.api.model.localization.TitleTranslationHelper;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 06. 23.
 */
public interface JpaTitleTranslationHelper extends TitleTranslationHelper<JpaTitleTranslation> {
    @Override
    default JpaTitleTranslation getTranslationInstance(String lngCode) {
        return new JpaTitleTranslation();
    }
}
