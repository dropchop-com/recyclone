package com.dropchop.recyclone.base.es.model.marker;

import com.dropchop.recyclone.base.es.model.localization.EsLanguage;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 05. 22.
 */
public interface HasEsLanguage {
  EsLanguage getLanguage();
  void setLanguage(EsLanguage language);
}
