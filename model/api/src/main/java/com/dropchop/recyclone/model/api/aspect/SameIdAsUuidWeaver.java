package com.dropchop.recyclone.model.api.aspect;

import com.dropchop.recyclone.model.api.marker.HasId;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 8. 01. 22.
 */
public interface SameIdAsUuidWeaver {

  @java.lang.SuppressWarnings("all")
  Logger log = LoggerFactory.getLogger(SameIdAsUuidWeaver.class);

  default void changeClassWithId(Object oModel, String id) {
    if (id == null) {
      log.warn("Id for [{}] is null will not set UUID.", oModel);
      return;
    }
    HasUuid model = (HasUuid)oModel;
    UUID oldId = model.getUuid();
    UUID uuid = UUID.fromString(id);
    if (uuid.equals(oldId)) {
      return;
    }
    log.trace("Will change uuid {} with {} based on id {}", model.getUuid(), uuid, id);
    model.setUuid(uuid);
  }

  default void changeClassWithUuid(Object oModel, UUID uuid) {
    if (uuid == null) {
      log.warn("UUID for [{}] is null will not set Id.", oModel);
      return;
    }
    HasId model = (HasId)oModel;
    String id = uuid.toString();
    String oldId = model.getId();
    if (id.equals(oldId)) {
      return;
    }
    log.trace("Will change id {} with {} based on uuid {}", model.getId(), id, uuid);
    model.setId(id);
  }
}
