package com.dropchop.recyclone.base.dto.model.aspect;

import com.dropchop.recyclone.base.api.model.aspect.SameIdAsUuidWeaver;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 8. 01. 22.
 */
@Aspect
@SuppressWarnings("SpringAopPointcutExpressionInspection")
public class SameIdAsUuidWeaverImpl implements SameIdAsUuidWeaver {

  @After(value = "set(String com.dropchop.recyclone.base.dto.model..id) " +
      "&& this(com.dropchop.recyclone.base.api.model.marker.HasId) " +
      "&& this(com.dropchop.recyclone.base.api.model.marker.HasUuid) " +
      "&& target(oModel) && args(id)",
    argNames = "oModel,id")
  public void changeClassWithId(Object oModel, String id) {
    SameIdAsUuidWeaver.super.changeClassWithId(oModel, id);
  }

  @After(value = "set(java.util.UUID com.dropchop.recyclone.base.dto.model..uuid) " +
      "&& this(com.dropchop.recyclone.base.api.model.marker.HasId) " +
      "&& this(com.dropchop.recyclone.base.api.model.marker.HasUuid) " +
      "&& target(oModel) && args(uuid)",
    argNames = "oModel,uuid")
  public void changeClassWithUuid(Object oModel, UUID uuid) {
    SameIdAsUuidWeaver.super.changeClassWithUuid(oModel, uuid);
  }
}
