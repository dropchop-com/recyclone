package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.Model;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 01. 22.
 */
public interface Action<T extends TitleTranslation>
  extends Model, HasCode, HasTitleTranslation<T> {
}
