package com.dropchop.recyclone.model.entity.es.base;

import com.dropchop.recyclone.model.api.localization.TitleTranslationHelper;
import com.dropchop.recyclone.model.entity.es.localization.EsTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 06. 23.
 */
public interface EsTitleTranslationHelper extends TitleTranslationHelper<EsTitleTranslation> {
    @Override
    default EsTitleTranslation getTranslationInstance(String lngCode) {
        return new EsTitleTranslation();
    }
}
