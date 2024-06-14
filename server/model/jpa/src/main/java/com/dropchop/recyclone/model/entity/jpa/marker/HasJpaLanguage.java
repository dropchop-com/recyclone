package com.dropchop.recyclone.model.entity.jpa.marker;

import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 05. 22.
 */
public interface HasJpaLanguage {
  JpaLanguage getLanguage();
  void setLanguage(JpaLanguage language);
}