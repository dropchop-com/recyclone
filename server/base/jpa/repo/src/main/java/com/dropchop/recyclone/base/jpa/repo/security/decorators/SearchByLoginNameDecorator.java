package com.dropchop.recyclone.base.jpa.repo.security.decorators;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.JoinType;
import com.dropchop.recyclone.base.dto.model.invoke.UserParams;
import com.dropchop.recyclone.base.jpa.model.security.JpaLoginAccount;
import com.dropchop.recyclone.base.api.repo.utils.SearchFields;
import com.dropchop.recyclone.base.jpa.repo.BlazeCriteriaDecorator;

public class SearchByLoginNameDecorator<E> extends BlazeCriteriaDecorator<E> {

  @Override
  public void decorate() {
    UserParams params = (UserParams) getContext().getParams();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    cb.join("accounts", "a", JoinType.LEFT);
    cb.where("TREAT(a AS " + JpaLoginAccount.class.getSimpleName() + ")"
        + DELIM + SearchFields.User.LOGIN_NAME).eq(params.getLoginName());
  }
}
