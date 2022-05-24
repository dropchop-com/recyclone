package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.localization.Language;
import com.dropchop.recyclone.model.api.localization.Translation;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 01. 22.
 */
public interface HasTranslation<T extends Translation> {

  Set<T> getTranslations();
  void setTranslations(Set<T> translations);

  default void addTranslation(T translation) {
    Set<T> translations = this.getTranslations();
    if (translations == null) {
      translations = new HashSet<>();
      this.setTranslations(translations);
    }
    translations.remove(translation);
    translations.add(translation);
  }


  default boolean removeTranslation(T translation) {
    /*Set<T> newTranslations = new LinkedHashSet<>(this.getTranslations());
    newTranslations.remove(translation);
    this.setTranslations(newTranslations);*/
    return this.getTranslations().remove(translation);
  }


  default boolean removeTranslation(Locale lng) {
    T t = this.getTranslation(lng);
    return this.removeTranslation(t);
  }

  default boolean removeTranslation(String languageTag) {
    T translation = this.getTranslation(languageTag);
    if (translation != null) {
      return this.getTranslations().remove(translation);
    }
    return false;
  }

  default T getTranslation(String tag) {
    Set<T> translations = this.getTranslations();
    if (translations == null) {
      return null;
    }
    for (T translation : translations) {
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

  default T getTranslation(Locale locale) {
    return getTranslation(Language.tagFromLocale(locale));
  }

  default T getTranslation(Language<?> language) {
    return getTranslation(language.toTag());
  }
}
