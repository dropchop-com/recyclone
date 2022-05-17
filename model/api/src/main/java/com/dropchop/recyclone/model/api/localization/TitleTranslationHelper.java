package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 05. 22.
 */
public interface TitleTranslationHelper<T extends TitleTranslation> extends TranslationHelper<T>, HasTitleTranslation<T> {

  default void setTitle(String langCode, String title) {
    T t = getTranslation(langCode);
    if (t == null) {
      t = getTranslationInstance(langCode);
      this.getTranslations().add(t);
    }
    t.setLang(langCode);
    t.setTitle(title);
  }
}
