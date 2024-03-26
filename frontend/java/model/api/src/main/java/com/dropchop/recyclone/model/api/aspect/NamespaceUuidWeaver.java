package com.dropchop.recyclone.model.api.aspect;

import com.dropchop.recyclone.model.api.marker.HasIdSameAsUuid;
import com.dropchop.recyclone.model.api.marker.HasUuidV3;
import com.dropchop.recyclone.model.api.utils.Uuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
public interface NamespaceUuidWeaver {

  @SuppressWarnings("all")
  Logger log = LoggerFactory.getLogger(NamespaceUuidWeaver.class);

  default void changeClassWithName(Object oModel, String name) {
    if (name == null) {
      log.warn("Name for [{}] is null will not set UUID.", oModel);
      return;
    }
    HasUuidV3 model = (HasUuidV3)oModel;
    UUID newUuid = Uuid.getNameBasedV3(oModel.getClass(), name);
    log.trace("Will change uuid {} with {} based on name {}", model.getUuid(), newUuid, name);
    model.setUuid(newUuid);
  }

  default void changeClassWithCode(Object oModel, String code) {
    if (code == null) {
      log.warn("Code for [{}] is null will not set UUID.", oModel);
      return;
    }
    HasUuidV3 model = (HasUuidV3)oModel;
    UUID newUuid = Uuid.getNameBasedV3(oModel.getClass(), code);
    log.trace("Will change uuid {} with {} based on code {}", model.getUuid(), newUuid, code);
    model.setUuid(newUuid);
    log.trace("Changed uuid to {} based on code {}", model.getUuid(), code);
    if (model instanceof HasIdSameAsUuid) {
      log.trace("Changed id to {} based on code {}", ((HasIdSameAsUuid) model).getId(), code);
    }
  }
}
