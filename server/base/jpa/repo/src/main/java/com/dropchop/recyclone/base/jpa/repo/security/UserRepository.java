package com.dropchop.recyclone.base.jpa.repo.security;


import com.dropchop.recyclone.base.dto.model.invoke.UserParams;
import com.dropchop.recyclone.base.jpa.model.security.JpaRoleNode;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import com.dropchop.recyclone.base.jpa.repo.*;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.RoleNodeParamsDecorator;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.SearchByLoginNameDecorator;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.SearchByTokenDecorator;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.SearchByUserDetailsDecorator;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@ApplicationScoped
public class UserRepository extends BlazeRepository<JpaUser, UUID> {

  Class<JpaUser> rootClass = JpaUser.class;

  protected <S extends JpaUser> Collection<BlazeCriteriaDecorator<S>> getCommonCriteriaDecorators() {
    return List.of(
      new LikeIdentifiersCriteriaDecorator<>(),
      new SortCriteriaDecorator<>(),
      new PageCriteriaDecorator<>(),
      new ByTagsCriteriaDecorator<>(),
      new SearchByUserDetailsDecorator<>()
    );
  }
}
