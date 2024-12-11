package com.dropchop.recyclone.model.api.marker;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 05. 22.
 */
public interface HasTranslationInlinedTitle extends HasTranslationInlined, HasTitle {

  default void setTitle(String langCode, String title) {
    this.setTitle(title);
    this.setLang(langCode);
  }
}
