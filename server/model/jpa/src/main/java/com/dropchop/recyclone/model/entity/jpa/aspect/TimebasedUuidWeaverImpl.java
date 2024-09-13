package com.dropchop.recyclone.model.entity.jpa.aspect;

import com.dropchop.recyclone.model.api.aspect.TimebasedUuidWeaver;
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

  @After(value = "set(java.time.ZonedDateTime com.dropchop.recyclone.model.entity..created) " +
      "&& this(com.dropchop.recyclone.model.api.marker.state.HasCreated) " +
      "&& this(com.dropchop.recyclone.model.api.marker.HasUuidV1) " +
      "&& target(oModel) && args(created)",
    argNames = "oModel,created")
  public void changeClassWithCreated(Object oModel, ZonedDateTime created) {
    TimebasedUuidWeaver.super.changeClassWithCreated(oModel, created);
  }

  @After(value = "set(java.util.UUID com.dropchop.recyclone.model.entity..uuid) " +
      "&& this(com.dropchop.recyclone.model.api.marker.state.HasCreated) " +
      "&& this(com.dropchop.recyclone.model.api.marker.HasUuidV1) " +
      "&& target(oModel) && args(uuid)",
    argNames = "oModel,uuid")
  public void changeClassWithUuid(Object oModel, UUID uuid) {
    TimebasedUuidWeaver.super.changeClassWithUuid(oModel, uuid);
  }
}
