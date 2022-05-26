package com.dropchop.recyclone.model.entity.jpa.marker;

import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 05. 22.
 */
public interface HasELanguage {
  ELanguage getLanguage();
  void setLanguage(ELanguage language);
}
