package com.dropchop.recyclone.base.dto.model.aspect;

import com.dropchop.recyclone.base.api.model.aspect.TimebasedUuidWeaver;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@Aspect
@SuppressWarnings("SpringAopPointcutExpressionInspection")
public class TimebasedUuidWeaverImpl implements TimebasedUuidWeaver {

  @After(value = "set(java.time.ZonedDateTime com.dropchop.recyclone.base.dto.model..created) " +
      "&& this(com.dropchop.recyclone.base.api.model.marker.state.HasCreated) " +
      "&& this(com.dropchop.recyclone.base.api.model.marker.HasUuidV1) " +
      "&& target(oModel) && args(created)",
    argNames = "oModel,created")
  public void changeClassWithCreated(Object oModel, ZonedDateTime created) {
    TimebasedUuidWeaver.super.changeClassWithCreated(oModel, created);
  }

  @After(value = "set(java.util.UUID com.dropchop.recyclone.base.dto.model..uuid) " +
      "&& this(com.dropchop.recyclone.base.api.model.marker.state.HasCreated) " +
      "&& this(com.dropchop.recyclone.base.api.model.marker.HasUuidV1) " +
      "&& target(oModel) && args(uuid)",
    argNames = "oModel,uuid")
  public void changeClassWithUuid(Object oModel, UUID uuid) {
    TimebasedUuidWeaver.super.changeClassWithUuid(oModel, uuid);
  }
}
