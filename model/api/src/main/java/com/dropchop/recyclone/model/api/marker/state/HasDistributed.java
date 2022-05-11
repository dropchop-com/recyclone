package com.dropchop.recyclone.model.api.marker.state;

import com.dropchop.recyclone.model.api.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
public interface HasDistributed extends HasStateInlinedCommon {

  State.Code.Distributed distributed = State.Code.distributed;

  ZonedDateTime getDistributed();
  void setDistributed(ZonedDateTime distributed);
}
