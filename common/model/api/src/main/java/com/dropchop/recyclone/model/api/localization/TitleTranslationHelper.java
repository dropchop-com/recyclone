package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 05. 22.
 */
public interface TitleTranslationHelper<TT extends TitleTranslation> extends TranslationHelper<TT>, HasTitleTranslation<TT> {

  default void setTitleTranslation(String langCode, String title) {
    TT t = getTranslation(langCode);
    if (t == null) {
      t = getTranslationInstance(langCode);
      if (t instanceof HasCreated c) {
        c.setCreated(ZonedDateTime.now());
      }
    }
    t.setLang(langCode);
    t.setTitle(title);
    if (t instanceof HasModified m) {
      m.setModified(ZonedDateTime.now());
    }
    this.addTranslation(t);
  }
}
