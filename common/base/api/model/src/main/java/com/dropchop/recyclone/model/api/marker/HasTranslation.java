package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.localization.Language;
import com.dropchop.recyclone.model.api.localization.Translation;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 01. 22.
 */
@SuppressWarnings("unused")
public interface HasTranslation<TR extends Translation> {

  Set<TR> getTranslations();
  void setTranslations(Set<TR> translations);

  default void addTranslation(TR translation) {
    Set<TR> translations = this.getTranslations();
    if (translations == null) {
      translations = new HashSet<>();
      this.setTranslations(translations);
    }
    translations.remove(translation);
    translations.add(translation);
  }


  default boolean removeTranslation(TR translation) {
    return this.getTranslations().remove(translation);
  }


  default boolean removeTranslation(Locale lng) {
    TR t = this.getTranslation(lng);
    return this.removeTranslation(t);
  }

  default boolean removeTranslation(String languageTag) {
    TR translation = this.getTranslation(languageTag);
    if (translation != null) {
      return this.getTranslations().remove(translation);
    }
    return false;
  }

  default TR getTranslation(String tag) {
    Set<TR> translations = this.getTranslations();
    if (translations == null) {
      return null;
    }
    for (TR translation : translations) {
      if (translation == null) {
        continue;
      }
      String transLanguage = translation.getLang();
      if (transLanguage == null) {
        continue;
      }
      if (transLanguage.equals(tag)) {
        return translation;
      }
    }
    return null;
  }

  default TR getTranslation(Locale locale) {
    return getTranslation(Language.tagFromLocale(locale));
  }

  default TR getTranslation(Language<?> language) {
    return getTranslation(language.toTag());
  }
}
