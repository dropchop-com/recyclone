package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.tagging.Tag;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
public interface HasTags<T extends TitleTranslation> {
  List<Tag<T>> getTags();
  void setTags(List<Tag<T>> tags);
}
