package com.dropchop.recyclone.base.jpa.repo;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.base.api.model.invoke.CommonParams;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 03. 22.
 */
@Slf4j
@SuppressWarnings("unused")
public class SortCriteriaDecorator<E> extends BlazeCriteriaDecorator<E> {

  private boolean nullsFirst = false;

  public SortCriteriaDecorator<E> nullsFirst() {
    this.nullsFirst = true;
    return this;
  }

  public SortCriteriaDecorator<E> nullsLast() {
    this.nullsFirst = false;
    return this;
  }

  @Override
  public void decorate() {
    CommonParams<?, ?, ?, ?> parameters = commonParamsGet();
    if (parameters == null) {
      return;
    }
    ResultFilter<?, ?> resultFilter = parameters.getFilter();
    List<String> sortFieldList = resultFilter.getSort();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
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
