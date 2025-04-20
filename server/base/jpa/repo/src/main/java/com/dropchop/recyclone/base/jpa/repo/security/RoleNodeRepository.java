package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNode;
import com.dropchop.recyclone.base.jpa.repo.*;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.RoleNodeParamsDecorator;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Getter
@ApplicationScoped
public class RoleNodeRepository extends BlazeRepository<JpaRoleNode, UUID> {

  Class<JpaRoleNode> rootClass = JpaRoleNode.class;

  protected <S extends JpaRoleNode> Collection<BlazeCriteriaDecorator<S>> getCommonCriteriaDecorators() {
    return List.of(
      new LikeIdentifiersCriteriaDecorator<>(),
      new SortCriteriaDecorator<>(),
      new PageCriteriaDecorator<>(),
      new RoleNodeParamsDecorator<>()
    );
  }
}
