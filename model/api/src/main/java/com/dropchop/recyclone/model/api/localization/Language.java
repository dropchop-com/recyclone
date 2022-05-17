package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasEmbededTitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

import java.util.Locale;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 01. 22.
 */
public interface Language<T extends TitleTranslation>
  extends Model, HasCode, HasEmbededTitleTranslation, HasTitleTranslation<T> {

  /**
   * ISO 15924 4-letter script code
   */
  enum Script {
    Latn,
    Cyrl
  }

  enum Variant {
    standard,
    nonstandard
  }

  String getLangCode();

  Script getScript();

  String getRegion();

  String getVariant();

  static String tagFromLocale(Locale locale) {
    return locale.toLanguageTag();
  }

  static String tagFromComponents(Language<?> language) {
    StringBuilder builder = new StringBuilder();
    String lang = language.getLangCode();
    if (lang == null) {
      return "";
    }
    builder.append(lang.toLowerCase());
    Script script = language.getScript();
    if (script != null) {
      builder.append('-');
      builder.append(script.name());
    }
    String region = language.getRegion();
    if (region != null && !region.isBlank()) {
      builder.append('-');
      builder.append(region);
    }
    String variant = language.getVariant();
    if (variant != null && !variant.isBlank()) {
      builder.append('-');
      builder.append(variant);
    }
    return builder.toString();
  }

  default String tagFromComponents() {
    return tagFromComponents(this);
  }

  static String tagFromLanguage(Language<?> language) {
    return language.getCode();
  }

  default Locale toLocale() {
    Locale.Builder builder = new Locale.Builder();
    builder.setLanguage(getLangCode());
    Script script = this.getScript();
    if (script != null) {
      builder.setScript(script.name());
    }
    String region = this.getRegion();
    if (region != null) {
      builder.setRegion(region);
    }
    String variant = this.getVariant();
    if (variant != null) {
      builder.setVariant(variant);
    }
    return builder.build();
  }

  static void fromTag(Language<?> language, String bcpTag) {
    language.setCode(bcpTag);
  }

  static void fromLocale(Language<?> language, Locale locale) {
    fromTag(language, locale.toLanguageTag());
  }

  default void fromLocale(Locale locale) {
    fromTag(this, locale.toLanguageTag());
  }

  default String toTag() {
    return tagFromLanguage(this);
  }

  default void fromTag(String bcpTag) {
    fromTag(this, bcpTag);
  }
}
