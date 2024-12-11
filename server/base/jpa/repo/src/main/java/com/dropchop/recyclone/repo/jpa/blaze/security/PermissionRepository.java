package com.dropchop.recyclone.repo.jpa.blaze.security;

import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import com.dropchop.recyclone.repo.jpa.blaze.LikeIdentifiersCriteriaDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.PageCriteriaDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.SortCriteriaDecorator;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
public class PermissionRepository extends BlazeRepository<JpaPermission, UUID> {

  Class<JpaPermission> rootClass = JpaPermission.class;

}
