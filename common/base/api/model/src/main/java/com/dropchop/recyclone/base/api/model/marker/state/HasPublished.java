package com.dropchop.recyclone.base.api.model.marker.state;

import com.dropchop.recyclone.base.api.model.base.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@SuppressWarnings("unused")
public interface HasPublished extends HasStateInlinedCommon {

  State.Code.Published published = State.Code.published;

  ZonedDateTime getPublished();
  void setPublished(ZonedDateTime published);
}