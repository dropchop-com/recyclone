package com.dropchop.recyclone.model.entity.es.marker;

import com.dropchop.recyclone.model.entity.es.localization.EsLanguage;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 05. 22.
 */
public interface HasEsLanguage {
  EsLanguage getLanguage();
  void setLanguage(EsLanguage language);
}
