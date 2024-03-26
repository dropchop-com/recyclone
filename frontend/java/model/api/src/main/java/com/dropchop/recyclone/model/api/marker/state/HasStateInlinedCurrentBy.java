package com.dropchop.recyclone.model.api.marker.state;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 22.
 */
@SuppressWarnings("unused")
public interface HasStateInlinedCurrentBy extends HasStateInlined {
  String getCurrentStateBy();
}
