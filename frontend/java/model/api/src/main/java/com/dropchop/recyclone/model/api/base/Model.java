package com.dropchop.recyclone.model.api.base;

import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasId;
import com.dropchop.recyclone.model.api.marker.HasIdSameAsUuid;
import com.dropchop.recyclone.model.api.marker.HasUuid;

import java.io.Serializable;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 12. 21.
 */
public interface Model extends Serializable, Cloneable {

  static String identifier(Model e) {
    if (e == null) {
      return null;
    }
    if (e instanceof HasId) {
      return ((HasId) e).getId();
    } else if (e instanceof HasCode) {
      return ((HasCode) e).getCode();
    } else if (e instanceof HasUuid) {
      return ((HasUuid) e).getUuid().toString();
    }
    throw new RuntimeException("Unsupported Entity type [" + e + "]");
  }

  default String identifier() {
    return identifier(this);
  }

  static String identifierField(Model e) {
    if (e == null) {
      return null;
    }
    if (e instanceof HasCode) {
      return "code";
    } else if (e instanceof HasUuid && !(e instanceof HasIdSameAsUuid)) {
      return "uuid";
    } else if (e instanceof HasId) {
      return "id";
    }
    throw new RuntimeException("Unsupported Entity type [" + e + "]");
  }

  default String identifierField() {
    return identifierField(this);
  }
}
