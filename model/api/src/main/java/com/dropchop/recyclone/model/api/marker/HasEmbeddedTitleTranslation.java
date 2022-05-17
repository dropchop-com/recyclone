package com.dropchop.recyclone.model.api.marker;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 05. 22.
 */
public interface HasEmbeddedTitleTranslation extends HasEmbeddedTranslation, HasTitle {

  default void setTitle(String langCode, String title) {
    this.setTitle(title);
    this.setLang(langCode);
  }
}
