package com.dropchop.recyclone.base.api.model.marker.state;

import com.dropchop.recyclone.base.api.model.base.State;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
public interface HasModified extends HasStateInlinedCommon {

  State.Code.Modified modified = State.Code.modified;

  ZonedDateTime getModified();
  void setModified(ZonedDateTime modified);
}
