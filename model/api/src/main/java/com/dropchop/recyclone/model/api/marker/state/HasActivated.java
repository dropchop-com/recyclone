package com.dropchop.recyclone.model.api.marker.state;

import com.dropchop.recyclone.model.api.base.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
public interface HasActivated extends HasStateInlinedCommon {

  State.Code.Activated activated = State.Code.activated;

  ZonedDateTime getActivated();
  void setActivated(ZonedDateTime activated);
}
