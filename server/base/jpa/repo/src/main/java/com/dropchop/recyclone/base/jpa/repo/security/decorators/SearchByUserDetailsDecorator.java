package com.dropchop.recyclone.base.jpa.repo.security.decorators;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.JoinType;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.api.repo.utils.SearchFields;
import com.dropchop.recyclone.base.dto.model.invoke.UserParams;
import com.dropchop.recyclone.base.jpa.model.security.JpaLoginAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaTokenAccount;
import com.dropchop.recyclone.base.jpa.repo.BlazeCriteriaDecorator;

public class SearchByUserDetailsDecorator<E> extends BlazeCriteriaDecorator<E> {

  @Override
  public void decorate() {
    String alias = getContext().getRootAlias();
    Params params = getContext().getParams();
    if (params instanceof UserParams userParams) {
      String loginName = userParams.getLoginName();
      String firstName = userParams.getFirstName();
      String lastName = userParams.getLastName();
      String email = userParams.getEmail();
      String searchTerm = userParams.getSearchTerm();

      CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
      if (loginName != null && !loginName.isBlank()) {
        cb.join("accounts", "a", JoinType.LEFT);
        cb.where("TREAT(a AS " + JpaLoginAccount.class.getSimpleName() + ")"
          + DELIM + SearchFields.User.LOGIN_NAME).eq(loginName);
      }
      if (firstName != null && !firstName.isBlank()) {
        cb.where(alias + DELIM + "firstName").eq(firstName);
      }
      if (lastName != null && !lastName.isBlank()) {
        cb.where(alias + DELIM + "lastName").eq(lastName);
      }
      if (email != null && !email.isBlank()) {
        cb.where(alias + DELIM + "defaultEmail").eq(email);
      }
      if (searchTerm != null && !searchTerm.isBlank()) {
        cb.join("accounts", "a", JoinType.LEFT);
        cb.join("accounts", "t", JoinType.LEFT);
        cb.whereOr()
          .where("LOWER(" + alias + DELIM + "firstName)").like().value(searchTerm.toLowerCase() + '%').noEscape()
          .where("LOWER(" + alias + DELIM + "lastName)").like().value(searchTerm.toLowerCase() + '%').noEscape()
          .where("LOWER(" + alias + DELIM + "defaultEmail)").like().value(searchTerm.toLowerCase() + '%').noEscape()
          .where("TREAT(a AS " + JpaLoginAccount.class.getSimpleName() + ")"
            + DELIM + SearchFields.User.LOGIN_NAME).eq(searchTerm)
          .where("TREAT(t AS " + JpaTokenAccount.class.getSimpleName() + ")"
            + DELIM + SearchFields.User.TOKEN).eq(searchTerm).endOr();
      }
    }
  }
}
