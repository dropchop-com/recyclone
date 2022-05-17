package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.localization.Language;

import java.util.Locale;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 01. 22.
 */
public interface HasTitleTranslation<T extends TitleTranslation>
  extends HasTranslation<T> {

  default void setTitle(String langCode, String title) {
    if (this instanceof HasEmbededTitleTranslation) {
      ((HasEmbededTitleTranslation) this).setTitle(title);
      ((HasEmbededTitleTranslation) this).setLang(langCode);
    } else {

    }
  }

  @Override
  void setTranslations(Set<T> translations);

  default String getTranslation(String langCode, String defaultTitle) {
    T trans = this.getTranslation(langCode);
    if (trans == null) {
      return defaultTitle;
    }
    return trans.getTitle();
  }

  default String getTranslationOrTitle(String langCode, String defaultTitle) {
    T trans = this.getTranslation(langCode);
    String title;
    if (trans == null) {
      if (this instanceof HasEmbededTitleTranslation) {
        title = ((HasEmbededTitleTranslation) this).getTitle();
      } else {
        return defaultTitle;
      }
    } else {
      title = trans.getTitle();
    }
    if (title == null) {
      title = defaultTitle;
    }
    return title;
  }

  default String getTranslationOrTitle(String tag) {
    return getTranslationOrTitle(tag, null);
  }

  default String getTranslationOrTitle(Language<?> language, String defaultTitle) {
    return getTranslationOrTitle(language.toTag(), defaultTitle);
  }

  default String getTranslationOrTitle(Language<?> language) {
    return getTranslationOrTitle(language.toTag(), null);
  }

  default String getTranslationOrTitle(Locale locale, String defaultTitle) {
    return getTranslationOrTitle(Language.tagFromLocale(locale), defaultTitle);
  }

  default String getTranslationOrTitle(Locale locale) {
    return getTranslationOrTitle(Language.tagFromLocale(locale), null);
  }
}
