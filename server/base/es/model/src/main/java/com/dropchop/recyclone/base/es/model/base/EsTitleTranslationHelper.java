package com.dropchop.recyclone.base.es.model.base;

import com.dropchop.recyclone.base.api.model.localization.TitleTranslationHelper;
import com.dropchop.recyclone.base.es.model.localization.EsTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 06. 23.
 */
public interface EsTitleTranslationHelper extends TitleTranslationHelper<EsTitleTranslation> {
    @Override
    default EsTitleTranslation getTranslationInstance(String lngCode) {
        return new EsTitleTranslation();
    }
}
