package com.dropchop.recyclone.base.api.model.localization;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.HasTranslationInlined;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
public interface Translated extends Model, HasId, HasTranslationInlined {
  String getGroup();
  void setGroup(String group);
}
