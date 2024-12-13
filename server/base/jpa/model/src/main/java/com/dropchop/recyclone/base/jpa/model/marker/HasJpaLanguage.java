package com.dropchop.recyclone.base.jpa.model.marker;

import com.dropchop.recyclone.base.jpa.model.localization.JpaLanguage;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 05. 22.
 */
public interface HasJpaLanguage {
  JpaLanguage getLanguage();
  void setLanguage(JpaLanguage language);
}
