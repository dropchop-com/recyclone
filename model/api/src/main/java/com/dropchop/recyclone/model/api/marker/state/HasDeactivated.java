package com.dropchop.recyclone.model.api.marker.state;

import com.dropchop.recyclone.model.api.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
public interface HasDeactivated extends HasStateInlinedCommon {

  State.Code.Deactivated deactivated = State.Code.deactivated;

  ZonedDateTime getDeactivated();
  void setDeactivated(ZonedDateTime deactivated);
}
