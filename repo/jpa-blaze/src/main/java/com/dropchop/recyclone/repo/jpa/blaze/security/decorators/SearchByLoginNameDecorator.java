package com.dropchop.recyclone.repo.jpa.blaze.security.decorators;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.JoinType;
import com.dropchop.recyclone.model.dto.security.params.SearchPrincipalParameters;
import com.dropchop.recyclone.repo.api.utils.SearchFields;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeCriteriaDecorator;


public class SearchByLoginNameDecorator extends BlazeCriteriaDecorator {


  @Override
  public void decorate() {
    SearchPrincipalParameters params = (SearchPrincipalParameters) getContext().getParams();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    cb.join("accounts", "a", JoinType.LEFT);
    cb.where("TREAT(a AS ELoginAccount)"  + DELIM + SearchFields.User.LOGIN_NAME).eq(params.getLoginName());
  }
}
