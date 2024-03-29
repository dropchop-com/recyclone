package com.dropchop.recyclone.model.api.marker.state;

import com.dropchop.recyclone.model.api.base.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
public interface HasCreated extends HasStateInlinedCommon {

  State.Code.Created created = State.Code.created;

  ZonedDateTime getCreated();
  void setCreated(ZonedDateTime created);
}
