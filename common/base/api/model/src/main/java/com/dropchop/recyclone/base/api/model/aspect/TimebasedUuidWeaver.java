package com.dropchop.recyclone.base.api.model.aspect;

import com.dropchop.recyclone.base.api.model.marker.HasUuidV1;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.utils.Uuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
public interface TimebasedUuidWeaver {

  @java.lang.SuppressWarnings("all")
  Logger log = LoggerFactory.getLogger(TimebasedUuidWeaver.class);

  default void changeClassWithCreated(Object oModel, ZonedDateTime created) {
    if (created == null) {
      log.warn("Created for [{}] is null will not set UUID.", oModel);
      return;
    }
    HasUuidV1 model = (HasUuidV1)oModel;
    UUID oldUuid = model.getUuid();
    if (oldUuid == null || oldUuid.version() != 1) {
      UUID newUuid = Uuid.fromTimeAndName(created.toInstant(), Uuid.getRandom());
      log.trace("Will change uuid {} with {} based on created {}", model.getUuid(), newUuid, created);
      model.setUuid(newUuid);
    } else {
      log.trace("Will not change uuid {} based on created {}", model.getUuid(), created);
    }
  }

  default void changeClassWithUuid(Object oModel, UUID uuid) {
    if (uuid == null) {
      log.warn("Uuid for [{}] is null will not set ZonedDateTime created.", oModel);
      return;
    }
    HasUuidV1 model = (HasUuidV1)oModel;
    if (uuid.version() == 1) {
      Instant instant = Uuid.toInstant(uuid);
      ZonedDateTime created = instant.atZone(ZoneId.systemDefault());
      log.trace("Will change created {} with {} based on uuid {}", model.getUuid(), created, uuid);
      ((HasCreated)model).setCreated(created);
    }
  }
}
