package com.dropchop.recyclone.repo.jpa.blaze.security;


import com.dropchop.recyclone.model.dto.invoke.UserParams;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeExecContext;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import com.dropchop.recyclone.repo.jpa.blaze.security.decorators.SearchByLoginNameDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.security.decorators.SearchByTokenDecorator;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
@SuppressWarnings("unused")
public class UserRepository extends BlazeRepository<JpaUser, UUID> {

  private static final JpaUser E_UUID_E_USER = new JpaUser();

  @Override
  @SuppressWarnings("unchecked")
  public Class<JpaUser> getRootClass() {
    return (Class<JpaUser>) E_UUID_E_USER.getClass();
  }


  public JpaUser findByLoginName(String loginName) {
    UserParams parameters = new UserParams();
    parameters.setLoginName(loginName);
    BlazeExecContext<JpaUser> blazeExecContext = new BlazeExecContext<>();
    blazeExecContext.setParams(parameters);
    blazeExecContext.decorateWith(new SearchByLoginNameDecorator());
    List<JpaUser> userList = this.find(blazeExecContext);
    return userList.isEmpty() ? null : userList.get(0);
  }


  public JpaUser findByToken(String token) {
    UserParams parameters = new UserParams();
    parameters.setToken(token);
    BlazeExecContext<JpaUser> blazeExecContext = new BlazeExecContext<>();
    blazeExecContext.setParams(parameters);
    blazeExecContext.decorateWith(new SearchByTokenDecorator());
    List<JpaUser> userList = this.find(blazeExecContext);
    return userList.isEmpty() ? null : userList.get(0);
  }
}
