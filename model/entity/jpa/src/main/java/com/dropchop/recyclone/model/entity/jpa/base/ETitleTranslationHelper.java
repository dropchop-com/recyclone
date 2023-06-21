package com.dropchop.recyclone.model.entity.jpa.base;

import com.dropchop.recyclone.model.api.localization.TitleTranslationHelper;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 06. 23.
 */
public interface ETitleTranslationHelper extends TitleTranslationHelper<ETitleTranslation> {
    @Override
    default ETitleTranslation getTranslationInstance(String lngCode) {
        return new ETitleTranslation();
    }
}
