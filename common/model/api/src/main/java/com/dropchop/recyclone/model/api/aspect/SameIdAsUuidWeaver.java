package com.dropchop.recyclone.model.api.aspect;

import com.dropchop.recyclone.model.api.marker.HasId;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 8. 01. 22.
 */
public interface SameIdAsUuidWeaver {

  @java.lang.SuppressWarnings("all")
  Logger log = LoggerFactory.getLogger(SameIdAsUuidWeaver.class);


  default void changeClassWithId(Object oModel, String id) {
    /*if (id == null) {
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
    model.setUuid(uuid);*/

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
    Field uuidField = WeaverCache.getField(oModel.getClass(), "uuid");
    if (uuidField == null) {
      log.warn("uuid field is missing in class hierarchy of [{}]", oModel.getClass());
      return;
    }
    uuidField.setAccessible(true);
    try {
      uuidField.set(oModel, uuid);
      log.trace("Changed uuid {} with {} based on id {}", model.getUuid(), uuid, id);
    } catch (Exception ex) {
      log.error("Unable to set [uuid] field in class hierarchy of [{}] with value [{}]", oModel.getClass(), id, ex);
    }
  }

  default void changeClassWithUuid(Object oModel, UUID uuid) {
    /*if (uuid == null) {
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
    model.setId(id);*/
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

    Field idField = WeaverCache.getField(oModel.getClass(), "id");
    if (idField == null) {
      log.warn("id field is missing in class hierarchy of [{}]", oModel.getClass());
      return;
    }
    idField.setAccessible(true);
    try {
      idField.set(oModel, id);
      log.trace("Changed id {} with {} based on uuid {}", model.getId(), id, uuid);
    } catch (Exception ex) {
      log.error("Unable to set [uuid] field in class hierarchy of [{}] with value [{}]", oModel.getClass(), id, ex);
    }
  }
}
