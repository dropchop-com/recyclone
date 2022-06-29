package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.tagging.Tag;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
public interface HasTags<T extends Tag<TT>, TT extends TitleTranslation> {
  List<T> getTags();
  void setTags(List<T> tags);
}
