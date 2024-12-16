package com.dropchop.recyclone.base.api.model.localization;

import com.dropchop.recyclone.base.api.model.marker.HasTitleDescriptionTranslation;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 05. 22.
 */
@SuppressWarnings("unused")
public interface TitleDescriptionTranslationHelper<TDT extends TitleDescriptionTranslation>
  extends TranslationHelper<TDT>, HasTitleDescriptionTranslation<TDT> {

  default void setTitleDescriptionTranslation(String langCode, String title, String description) {
    TDT t = getTranslation(langCode);
    if (t == null) {
      t = getTranslationInstance(langCode);
      if (t instanceof HasCreated c) {
        c.setCreated(ZonedDateTime.now());
      }
    }
    t.setLang(langCode);
    t.setTitle(title);
    t.setDescription(description);
    this.addTranslation(t);
  }
}
