package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.marker.HasTranslationInlinedTitle;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 01. 22.
 */
public interface TitleTranslation extends Translation, HasTranslationInlinedTitle {
  String getTitle();
  void setTitle(String title);
}
