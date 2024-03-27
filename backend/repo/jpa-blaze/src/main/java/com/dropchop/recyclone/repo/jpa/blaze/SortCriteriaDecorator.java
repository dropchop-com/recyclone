package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.model.api.invoke.CommonParams;
import com.dropchop.recyclone.model.api.invoke.ResultFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 03. 22.
 */
@Slf4j
public class SortCriteriaDecorator extends BlazeCriteriaDecorator {

  private boolean nullsFirst = false;

  public SortCriteriaDecorator nullsFirst() {
    this.nullsFirst = true;
    return this;
  }

  public SortCriteriaDecorator nullsLast() {
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
