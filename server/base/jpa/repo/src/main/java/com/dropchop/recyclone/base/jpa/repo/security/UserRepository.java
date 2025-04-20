package com.dropchop.recyclone.base.jpa.repo.security;


import com.dropchop.recyclone.base.dto.model.invoke.UserParams;
import com.dropchop.recyclone.base.jpa.model.security.JpaUser;
import com.dropchop.recyclone.base.jpa.repo.BlazeExecContext;
import com.dropchop.recyclone.base.jpa.repo.BlazeRepository;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.SearchByLoginNameDecorator;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.SearchByTokenDecorator;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@ApplicationScoped
public class UserRepository extends BlazeRepository<JpaUser, UUID> {

  Class<JpaUser> rootClass = JpaUser.class;

  public JpaUser findByLoginName(String loginName) {
    UserParams parameters = new UserParams();
    parameters.setLoginName(loginName);
    BlazeExecContext<JpaUser> blazeExecContext = createRepositoryExecContext();
    blazeExecContext.setParams(parameters);
    blazeExecContext.decorateWith(new SearchByLoginNameDecorator<>());
    List<JpaUser> userList = this.find(blazeExecContext);
    return userList.isEmpty() ? null : userList.getFirst();
  }

  public JpaUser findByToken(String token) {
    UserParams parameters = new UserParams();
    parameters.setToken(token);
    BlazeExecContext<JpaUser> blazeExecContext = createRepositoryExecContext();
    blazeExecContext.setParams(parameters);
    blazeExecContext.decorateWith(new SearchByTokenDecorator<>());
    List<JpaUser> userList = this.find(blazeExecContext);
    return userList.isEmpty() ? null : userList.getFirst();
  }
}
