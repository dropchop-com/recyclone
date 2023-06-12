package com.dropchop.recyclone.repo.jpa.blaze.security;


import com.dropchop.recyclone.model.dto.security.params.SearchPrincipalParameters;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.security.EUser;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeExecContext;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import com.dropchop.recyclone.repo.jpa.blaze.security.decorators.SearchByLoginNameDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.security.decorators.SearchByTokenDecorator;

import javax.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

@ApplicationScoped
@RepositoryType(RCYN_DEFAULT)
public class UserRepository extends BlazeRepository<EUser, UUID> {


  @Override
  public Class<EUser> getRootClass() {
    return EUser.class;
  }


  public EUser<EUuid> findByLoginName(String loginName) {
    SearchPrincipalParameters parameters = new SearchPrincipalParameters();
    parameters.setLoginName(loginName);
    BlazeExecContext<EUser> blazeExecContext = new BlazeExecContext<>();
    blazeExecContext.setParams(parameters);
    blazeExecContext.decorateWith(new SearchByLoginNameDecorator());
    List<EUser> userList = this.find(blazeExecContext);
    return userList.isEmpty() ? null : userList.get(0);
  }


  public EUser<EUuid> findByToken(String token) {
    SearchPrincipalParameters parameters = new SearchPrincipalParameters();
    parameters.setToken(token);
    BlazeExecContext<EUser> blazeExecContext = new BlazeExecContext<>();
    blazeExecContext.setParams(parameters);
    blazeExecContext.decorateWith(new SearchByTokenDecorator());
    List<EUser> userList = this.find(blazeExecContext);
    return userList.isEmpty() ? null : userList.get(0);
  }
}
