package com.dropchop.recyclone.model.entity.es.base;

import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslationHelper;
import com.dropchop.recyclone.model.entity.es.localization.EsTitleDescriptionTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 06. 23.
 */
public interface EsTitleDescriptionTranslationHelper extends TitleDescriptionTranslationHelper<EsTitleDescriptionTranslation> {
    @Override
    default EsTitleDescriptionTranslation getTranslationInstance(String lngCode) {
        return new EsTitleDescriptionTranslation();
    }
}
