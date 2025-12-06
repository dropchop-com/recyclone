package com.dropchop.recyclone.base.jpa.repo.security;

import com.dropchop.recyclone.base.dto.model.invoke.UserAccountParams;
import com.dropchop.recyclone.base.jpa.model.security.JpaUserAccount;
import com.dropchop.recyclone.base.jpa.repo.*;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.SearchByLoginNameDecorator;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.SearchByTokenDecorator;
import com.dropchop.recyclone.base.jpa.repo.security.decorators.SearchByUserAccountDetailsDecorator;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@ApplicationScoped
public class UserAccountRepository extends BlazeRepository<JpaUserAccount, UUID> {

  Class<JpaUserAccount> rootClass = JpaUserAccount.class;

  protected <S extends JpaUserAccount> Collection<BlazeCriteriaDecorator<S>> getCommonCriteriaDecorators() {
    return List.of(
      new LikeIdentifiersCriteriaDecorator<>(),
      new SortCriteriaDecorator<>(),
      new PageCriteriaDecorator<>(),
      new ByTagsCriteriaDecorator<>(),
      new SearchByUserAccountDetailsDecorator<>()
    );
  }

  public JpaUserAccount findByLoginName(String loginName) {
    UserAccountParams parameters = new UserAccountParams();
    parameters.setLoginName(loginName);
    // TODO: Ota check this find by login name
    BlazeExecContext<JpaUserAccount> blazeExecContext = createRepositoryExecContext(null);
    blazeExecContext.setParams(parameters);
    blazeExecContext.decorateWith(new SearchByLoginNameDecorator<>());
    List<JpaUserAccount> userList = this.find(blazeExecContext);
    return userList.isEmpty() ? null : userList.getFirst();
  }

  public JpaUserAccount findByToken(String token) {
    UserAccountParams parameters = new UserAccountParams();
    parameters.setToken(token);
    // TODO: Ota check this find by token
    BlazeExecContext<JpaUserAccount> blazeExecContext = createRepositoryExecContext(null);
    blazeExecContext.setParams(parameters);
    blazeExecContext.decorateWith(new SearchByTokenDecorator<>());
    List<JpaUserAccount> userList = this.find(blazeExecContext);
    return userList.isEmpty() ? null : userList.getFirst();
  }
}
