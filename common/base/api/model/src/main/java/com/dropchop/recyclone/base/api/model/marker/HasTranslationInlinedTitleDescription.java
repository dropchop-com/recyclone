package com.dropchop.recyclone.base.api.model.marker;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 05. 22.
 */
@SuppressWarnings("unused")
public interface HasTranslationInlinedTitleDescription extends HasTranslationInlinedTitle, HasDescription {

  default void setTitleDescription(String langCode, String title, String description) {
    this.setTitle(title);
    this.setDescription(description);
    this.setLang(langCode);
  }
}
