package com.dropchop.recyclone.base.jpa.repo.security.decorators;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.JoinType;
import com.dropchop.recyclone.base.dto.model.invoke.UserParams;
import com.dropchop.recyclone.base.jpa.model.security.JpaTokenAccount;
import com.dropchop.recyclone.base.api.repo.utils.SearchFields;
import com.dropchop.recyclone.base.jpa.repo.BlazeCriteriaDecorator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchByTokenDecorator extends BlazeCriteriaDecorator {


  @Override
  public void decorate() {
    UserParams params = (UserParams)getContext().getParams();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    cb.join("accounts", "a", JoinType.LEFT);
    cb.where("TREAT(a AS " + JpaTokenAccount.class.getSimpleName() + ")"
        + DELIM + SearchFields.User.TOKEN).eq(params.getToken());
  }
}
