package com.dropchop.recyclone.base.api.model.localization;

import com.dropchop.recyclone.base.api.model.marker.HasTranslationInlinedTitleDescription;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 01. 22.
 */
public interface TitleDescriptionTranslated extends
    TitleTranslated, HasTranslationInlinedTitleDescription {
  String getDescription();
  void setDescription(String description);
}
