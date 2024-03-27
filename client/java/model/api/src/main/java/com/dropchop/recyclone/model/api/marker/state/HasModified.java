package com.dropchop.recyclone.model.api.marker.state;

import com.dropchop.recyclone.model.api.base.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
public interface HasModified extends HasStateInlinedCommon {

  State.Code.Modified modified = State.Code.modified;

  ZonedDateTime getModified();
  void setModified(ZonedDateTime modified);
}
