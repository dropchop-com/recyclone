package com.dropchop.recyclone.base.es.model.base;

import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslationHelper;
import com.dropchop.recyclone.base.es.model.localization.EsTitleDescriptionTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 06. 23.
 */
public interface EsTitleDescriptionTranslationHelper extends TitleDescriptionTranslationHelper<EsTitleDescriptionTranslation> {
    @Override
    default EsTitleDescriptionTranslation getTranslationInstance(String lngCode) {
        return new EsTitleDescriptionTranslation();
    }
}
