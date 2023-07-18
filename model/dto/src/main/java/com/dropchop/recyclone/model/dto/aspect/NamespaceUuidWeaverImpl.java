package com.dropchop.recyclone.model.dto.aspect;

import com.dropchop.recyclone.model.api.aspect.NamespaceUuidWeaver;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Aspect
@SuppressWarnings("SpringAopPointcutExpressionInspection")
public class NamespaceUuidWeaverImpl implements NamespaceUuidWeaver {

  @After(value = "set(String com.dropchop.recyclone.model.dto..name) " +
      "&& this(com.dropchop.recyclone.model.api.marker.HasName) " +
      "&& this(com.dropchop.recyclone.model.api.marker.HasUuidV3) " +
      "&& target(oModel) && args(name)",
    argNames = "oModel,name")
  public void changeClassWithName(Object oModel, String name) {
    NamespaceUuidWeaver.super.changeClassWithName(oModel, name);
  }

  @After(value = "set(String com.dropchop.recyclone.model.dto..code) " +
      "&& this(com.dropchop.recyclone.model.api.marker.HasCode) " +
      "&& this(com.dropchop.recyclone.model.api.marker.HasUuidV3) " +
      "&& target(oModel) && args(code)",
    argNames = "oModel,code")
  public void changeClassWithCode(Object oModel, String code) {
    NamespaceUuidWeaver.super.changeClassWithCode(oModel, code);
  }
}
