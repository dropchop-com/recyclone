package com.dropchop.recyclone.repo.jpa.blaze.security.decorators;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.JoinType;
import com.dropchop.recyclone.model.dto.invoke.UserParams;
import com.dropchop.recyclone.model.entity.jpa.security.JpaLoginAccount;
import com.dropchop.recyclone.repo.api.utils.SearchFields;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeCriteriaDecorator;


public class SearchByLoginNameDecorator extends BlazeCriteriaDecorator {

  @Override
  public void decorate() {
    UserParams params = (UserParams) getContext().getParams();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    cb.join("accounts", "a", JoinType.LEFT);
    cb.where("TREAT(a AS " + JpaLoginAccount.class.getSimpleName() + ")"
        + DELIM + SearchFields.User.LOGIN_NAME).eq(params.getLoginName());
  }
}
