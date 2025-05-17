package com.dropchop.recyclone.base.jpa.repo.security.decorators;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.JoinType;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.api.repo.utils.SearchFields;
import com.dropchop.recyclone.base.dto.model.invoke.UserAccountParams;
import com.dropchop.recyclone.base.jpa.model.security.JpaLoginAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaTokenAccount;
import com.dropchop.recyclone.base.jpa.repo.BlazeCriteriaDecorator;

public class SearchByUserAccountDetailsDecorator<E> extends BlazeCriteriaDecorator<E> {

  @Override
  public void decorate() {
    String alias = getContext().getRootAlias();
    Params params = getContext().getParams();
    if (params instanceof UserAccountParams userAccountParams) {

      String loginName = userAccountParams.getLoginName();
      String token = userAccountParams.getToken();

      CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
      cb.join("accounts", "a", JoinType.LEFT);
      cb.join("accounts", "t", JoinType.LEFT);
      if (loginName != null && !loginName.isBlank() && token != null && !token.isBlank()) {
        cb.whereOr()
          .where("TREAT(a AS " + JpaLoginAccount.class.getSimpleName() + ")"
            + DELIM + SearchFields.User.LOGIN_NAME).eq(loginName)
          .where("TREAT(t AS " + JpaTokenAccount.class.getSimpleName() + ")"
            + DELIM + SearchFields.User.TOKEN).eq(token);
      } else if (loginName != null && !loginName.isBlank()) {
        cb.where("TREAT(a AS " + JpaLoginAccount.class.getSimpleName() + ")"
          + DELIM + SearchFields.User.LOGIN_NAME).eq(loginName);
      } else if (token != null && !token.isBlank()) {
        cb.where("TREAT(a AS " + JpaLoginAccount.class.getSimpleName() + ")"
          + DELIM + SearchFields.User.TOKEN).eq(token);
      }
    }
  }
}


