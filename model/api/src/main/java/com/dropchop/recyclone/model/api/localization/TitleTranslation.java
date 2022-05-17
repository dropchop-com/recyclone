package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.marker.HasTitle;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 01. 22.
 */
public interface TitleTranslation extends Translation, HasTitle {
  String getTitle();
  void setTitle(String title);
}
