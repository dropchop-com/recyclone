package com.dropchop.recyclone.model.api.marker.state;

import com.dropchop.recyclone.model.api.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
public interface HasPublished extends HasStateInlinedCommon {

  State.Code.Published published = State.Code.published;

  ZonedDateTime getPublished();
  void setPublished(ZonedDateTime published);
}
