package com.dropchop.recyclone.model.dto.aspect;

import com.dropchop.recyclone.model.api.aspect.SameIdAsUuidWeaver;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 8. 01. 22.
 */
@Aspect
public class SameIdAsUuidWeaverImpl implements SameIdAsUuidWeaver {

  @After(value = "set(String com.dropchop.recyclone.model.dto..*id) " +
    "&& this(com.dropchop.recyclone.model.api.marker.HasId) && this(com.dropchop.recyclone.model.api.marker.HasUuid) " +
    "&& target(oModel) && args(id)",
    argNames = "oModel,id")
  public void changeClassWithId(Object oModel, String id) {
    SameIdAsUuidWeaver.super.changeClassWithId(oModel, id);
  }

  @After(value = "set(java.util.UUID com.dropchop.recyclone.model.dto..*uuid) " +
    "&& this(com.dropchop.recyclone.model.api.marker.HasId) && this(com.dropchop.recyclone.model.api.marker.HasUuid) " +
    "&& target(oModel) && args(uuid)",
    argNames = "oModel,uuid")
  public void changeClassWithUuid(Object oModel, UUID uuid) {
    SameIdAsUuidWeaver.super.changeClassWithUuid(oModel, uuid);
  }
}
