package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.model.api.invoke.CommonParams;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 03. 22.
 */
public class SortCriteriaDecorator<T, P extends CommonParams> extends BlazeCriteriaDecorator<T, P> {

  private boolean nullsFirst = false;

  public SortCriteriaDecorator<T, ? extends P> nullsFirst() {
    this.nullsFirst = true;
    return this;
  }

  public SortCriteriaDecorator<T, ? extends P> nullsLast() {
    this.nullsFirst = false;
    return this;
  }

  @Override
  public void decorate() {
    List<String> sortFieldList = getContext().getParams().getSort();
    CriteriaBuilder<T> cb = getContext().getCriteriaBuilder();
    String alias = getContext().getRootAlias();

    for (String sortField : sortFieldList) {
      if (sortField.startsWith("-")) {
        cb.orderBy(alias + "." + sortField.substring(1), false, nullsFirst);
      } else if (sortField.startsWith("+")) {
        cb.orderBy(alias + "." + sortField.substring(1), true, nullsFirst);
      } else {
        cb.orderBy(alias + "." + sortField, true, nullsFirst);
      }
    }
  }
}
