package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermission;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import com.dropchop.recyclone.repo.jpa.blaze.LikeIdentifiersCriteriaDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.PageCriteriaDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.SortCriteriaDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.security.decorators.RoleNodePermissionParamsDecorator;
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
public class RoleNodePermissionRepository extends BlazeRepository<JpaRoleNodePermission, UUID> {

  Class<JpaRoleNodePermission> rootClass = JpaRoleNodePermission.class;


  protected Collection<CriteriaDecorator> getCommonCriteriaDecorators() {
    return List.of(
      new LikeIdentifiersCriteriaDecorator(),
      new SortCriteriaDecorator(),
      new PageCriteriaDecorator(),
      new RoleNodePermissionParamsDecorator()
    );
  }

}
