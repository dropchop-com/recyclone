package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.model.entity.jpa.security.JpaPermissionInstance;
import com.dropchop.recyclone.base.jpa.repo.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
public class PermissionDetailsRepository extends BlazeRepository<JpaPermissionInstance, UUID> {

  Class<JpaPermissionInstance> rootClass = JpaPermissionInstance.class;
}
