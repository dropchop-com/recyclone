package com.dropchop.recyclone.model.entity.jpa.base;

import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslationHelper;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 06. 23.
 */
public interface ETitleDescriptionTranslationHelper extends TitleDescriptionTranslationHelper<ETitleDescriptionTranslation> {
    @Override
    default ETitleDescriptionTranslation getTranslationInstance(String lngCode) {
        return new ETitleDescriptionTranslation();
    }
}
