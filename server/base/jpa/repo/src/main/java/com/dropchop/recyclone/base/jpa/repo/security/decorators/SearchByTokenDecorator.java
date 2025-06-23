package com.dropchop.recyclone.base.jpa.repo.security.decorators;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.base.api.repo.utils.SearchFields;
import com.dropchop.recyclone.base.dto.model.invoke.UserAccountParams;
import com.dropchop.recyclone.base.jpa.model.security.JpaTokenAccount;
import com.dropchop.recyclone.base.jpa.repo.BlazeCriteriaDecorator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchByTokenDecorator<E> extends BlazeCriteriaDecorator<E> {

  @Override
  public void decorate() {
    String alias = getContext().getRootAlias();
    UserAccountParams params = (UserAccountParams)getContext().getParams();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    cb.where("TREAT(" + alias + " AS " + JpaTokenAccount.class.getSimpleName() + ")"
        + DELIM + SearchFields.User.TOKEN).eq(params.getToken());
  }
}
