package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.marker.HasTitleDescriptionTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 05. 22.
 */
public interface TitleDescriptionTranslationHelper<TDT extends TitleDescriptionTranslation>
  extends TranslationHelper<TDT>, HasTitleDescriptionTranslation<TDT> {

  default void setTitleDescription(String langCode, String title, String description) {
    TDT t = getTranslation(langCode);
    if (t == null) {
      t = getTranslationInstance(langCode);
      this.getTranslations().add(t);
    }
    t.setLang(langCode);
    t.setTitle(title);
    t.setDescription(description);
  }
}
