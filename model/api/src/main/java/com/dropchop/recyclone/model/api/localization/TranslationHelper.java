package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.marker.HasEmbeddedTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 05. 22.
 */
public interface TranslationHelper<T extends Translation> {

  T getTranslationInstance(String lngCode);
}
