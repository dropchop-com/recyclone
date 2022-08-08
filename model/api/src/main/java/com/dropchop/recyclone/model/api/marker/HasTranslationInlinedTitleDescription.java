package com.dropchop.recyclone.model.api.marker;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 05. 22.
 */
public interface HasTranslationInlinedTitleDescription extends HasTranslationInlinedTitle, HasDescription {

  default void setTitleDescription(String langCode, String title, String description) {
    this.setTitle(title);
    this.setDescription(description);
    this.setLang(langCode);
  }
}
