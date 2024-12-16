package com.dropchop.recyclone.base.api.model.marker.state;

import com.dropchop.recyclone.base.api.model.base.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
public interface HasCreated extends HasStateInlinedCommon {

  State.Code.Created created = State.Code.created;

  ZonedDateTime getCreated();
  void setCreated(ZonedDateTime created);
}
