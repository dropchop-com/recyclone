package com.dropchop.recyclone.base.api.model.base;

import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.HasIdSameAsUuid;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;

import java.io.Serializable;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 12. 21.
 */
public interface Model extends Serializable, Cloneable {

  static String identifier(Model e) {
    return switch (e) {
      case null -> null;
      case HasId hasId -> hasId.getId();
      case HasCode hasCode -> hasCode.getCode();
      case HasUuid hasUuid -> hasUuid.getUuid().toString();
      default -> throw new RuntimeException("Unsupported Entity type [" + e + "]");
    };
  }

  default String identifier() {
    return identifier(this);
  }

  static String identifier(Model e, String defaultValue) {
    return switch (e) {
      case null -> defaultValue;
      case HasId hasId -> hasId.getId();
      case HasCode hasCode -> hasCode.getCode();
      case HasUuid hasUuid -> hasUuid.getUuid().toString();
      default -> defaultValue;
    };
  }

  default String identifier(String defaultValue) {
    return identifier(this, defaultValue);
  }

  static String identifierField(Model e) {
    return switch (e) {
      case null -> null;
      case HasCode ignored -> "code";
      case HasUuid ignored when !(e instanceof HasIdSameAsUuid) -> "uuid";
      case HasId ignored -> "id";
      default -> throw new RuntimeException("Unsupported Entity type [" + e + "]");
    };
  }

  default String identifierField() {
    return identifierField(this);
  }
}
