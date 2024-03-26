package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.marker.HasTranslationInlinedTitleDescription;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 01. 22.
 */
public interface TitleDescriptionTranslation extends TitleTranslation, HasTranslationInlinedTitleDescription {
  String getDescription();
  void setDescription(String description);
}
