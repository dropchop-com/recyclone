package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNodePermission;
import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import com.dropchop.recyclone.base.jpa.repo.BlazeRepository;
import com.dropchop.recyclone.base.jpa.repo.LikeIdentifiersCriteriaDecorator;
import com.dropchop.recyclone.base.jpa.repo.PageCriteriaDecorator;
import com.dropchop.recyclone.base.jpa.repo.SortCriteriaDecorator;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.RoleNodePermissionParamsDecorator;
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
