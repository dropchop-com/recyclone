package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasAttributes;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 06. 22.
 */
public interface Params extends Model, HasAttributes {

  String getRequestId();
  void setRequestId(String requestId);
}
