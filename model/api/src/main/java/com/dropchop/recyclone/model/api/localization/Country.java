package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.Model;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
public interface Country<T extends TitleTranslation>
  extends Model, HasCode, HasTitleTranslation<T> {
}
