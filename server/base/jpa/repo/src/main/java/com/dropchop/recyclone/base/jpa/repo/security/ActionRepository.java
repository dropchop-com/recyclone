package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.model.entity.jpa.security.JpaAction;
import com.dropchop.recyclone.base.jpa.repo.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
public class ActionRepository extends BlazeRepository<JpaAction, String> {

  Class<JpaAction> rootClass = JpaAction.class;
}
