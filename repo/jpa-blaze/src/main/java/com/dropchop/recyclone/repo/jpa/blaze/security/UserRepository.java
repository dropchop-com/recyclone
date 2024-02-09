package com.dropchop.recyclone.repo.jpa.blaze.security;


import com.dropchop.recyclone.model.dto.invoke.UserParams;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.security.EUser;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeExecContext;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import com.dropchop.recyclone.repo.jpa.blaze.security.decorators.SearchByLoginNameDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.security.decorators.SearchByTokenDecorator;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

@ApplicationScoped
@RepositoryType(RCYN_DEFAULT)
public class UserRepository extends BlazeRepository<EUser<EUuid>, UUID> {

  private static final EUser<?> E_UUID_E_USER = new EUser<>();

  @Override
  @SuppressWarnings("unchecked")
  public Class<EUser<EUuid>> getRootClass() {
    return (Class<EUser<EUuid>>) E_UUID_E_USER.getClass();
  }


  public EUser<EUuid> findByLoginName(String loginName) {
    UserParams parameters = new UserParams();
    parameters.setLoginName(loginName);
    BlazeExecContext<EUser<EUuid>> blazeExecContext = new BlazeExecContext<>();
    blazeExecContext.setParams(parameters);
    blazeExecContext.decorateWith(new SearchByLoginNameDecorator());
    List<EUser<EUuid>> userList = this.find(blazeExecContext);
    return userList.isEmpty() ? null : userList.get(0);
  }


  public EUser<EUuid> findByToken(String token) {
    UserParams parameters = new UserParams();
    parameters.setToken(token);
    BlazeExecContext<EUser<EUuid>> blazeExecContext = new BlazeExecContext<>();
    blazeExecContext.setParams(parameters);
    blazeExecContext.decorateWith(new SearchByTokenDecorator());
    List<EUser<EUuid>> userList = this.find(blazeExecContext);
    return userList.isEmpty() ? null : userList.get(0);
  }
}
