package com.dropchop.recyclone.model.api.marker.state;

import com.dropchop.recyclone.model.api.base.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@SuppressWarnings("unused")
public interface HasDistributed extends HasStateInlinedCommon {

  State.Code.Distributed distributed = State.Code.distributed;

  ZonedDateTime getDistributed();
  void setDistributed(ZonedDateTime distributed);
}
