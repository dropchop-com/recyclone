package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasEmbeddedTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
public interface Translation extends Model, HasEmbeddedTranslation {

  /**
   * Set to true if this is the default translation for the owning object.
   *
   * Used only when translation is swapped in owning object. This can happen for instance if
   * client requests different language than the one set in owning object and the back-end swaps
   * default translation in owning object and marks this in set of translations,
   * so the client is able to detect default translation.
   *
   * @return Boolean.TRUE if this is the default translation, null or Boolean.FALSE otherwise.
   */
  Boolean getBase();
  void setBase(Boolean base);
}
