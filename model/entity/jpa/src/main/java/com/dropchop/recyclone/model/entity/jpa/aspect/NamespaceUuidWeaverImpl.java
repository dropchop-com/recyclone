package com.dropchop.recyclone.model.entity.jpa.aspect;

import com.dropchop.recyclone.model.api.aspect.NamespaceUuidWeaver;
import com.dropchop.recyclone.model.api.marker.HasUuidV3;
import com.dropchop.recyclone.model.api.utils.Uuid;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Aspect
@SuppressWarnings("SpringAopPointcutExpressionInspection")
public class NamespaceUuidWeaverImpl implements NamespaceUuidWeaver {

  public static void compute(Object oModel, String name) {
    HasUuidV3 model = (HasUuidV3)oModel;
    String newName = oModel.getClass().getSimpleName() + "." + name;
    UUID newUuid;
    if (newName.startsWith("E") && Character.isUpperCase(newName.charAt(1))) {
      newUuid = Uuid.getNameBasedV3(newName.substring(1));
    } else {
      newUuid = Uuid.getNameBasedV3(newName);
    }
    log.trace("Will change uuid {} with {} based on name {}", model.getUuid(), newUuid, name);
    model.setUuid(newUuid);
  }

  @After(value = "set(String com.dropchop.recyclone.model.entity..name) " +
      "&& this(com.dropchop.recyclone.model.api.marker.HasName) " +
      "&& this(com.dropchop.recyclone.model.api.marker.HasUuidV3) " +
      "&& target(oModel) && args(name)",
    argNames = "oModel,name")
  public void changeClassWithName(Object oModel, String name) {
    compute(oModel, name);
  }

  @After(value = "set(String com.dropchop.recyclone.model.entity..code) " +
      "&& this(com.dropchop.recyclone.model.api.marker.HasCode) " +
      "&& this(com.dropchop.recyclone.model.api.marker.HasUuidV3) " +
      "&& target(oModel) && args(code)",
    argNames = "oModel,code")
  public void changeClassWithCode(Object oModel, String code) {
    compute(oModel, code);
  }
}
