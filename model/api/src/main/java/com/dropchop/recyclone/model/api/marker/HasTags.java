package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.tag.Tag;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
public interface HasTags {
  List<Tag> getTags();
  void setTags(List<Tag> tags);
}
