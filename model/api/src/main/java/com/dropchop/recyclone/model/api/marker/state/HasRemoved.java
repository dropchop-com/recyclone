package com.dropchop.recyclone.model.api.marker.state;

import com.dropchop.recyclone.model.api.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
public interface HasRemoved extends HasStateInlinedCommon {

  State.Code.Removed removed = State.Code.removed;

  ZonedDateTime getRemoved();
  void setRemoved(ZonedDateTime removed);
}
