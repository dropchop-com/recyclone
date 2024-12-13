package com.dropchop.recyclone.base.api.model.marker.state;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 03. 22.
 */
@SuppressWarnings("unused")
public interface HasStateInlinedCurrentBy extends HasStateInlined {
  String getCurrentStateBy();
}
