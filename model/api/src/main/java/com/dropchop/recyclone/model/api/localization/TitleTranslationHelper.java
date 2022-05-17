package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 05. 22.
 */
public interface TitleTranslationHelper<T extends TitleTranslation> extends TranslationHelper<T>, HasTitleTranslation<T> {

  T getTranslationInstance();

  default void setTitle(String langCode, String title) {
    T t = getTranslationInstance();
    t.setTitle(title);
    t.setLang(langCode);
    this.getTranslations().add(t);
  }
}
