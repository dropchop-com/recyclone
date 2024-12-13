package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.base.api.model.invoke.CommonParams;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilterDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 03. 22.
 */
@Slf4j
public class PageCriteriaDecorator extends BlazeCriteriaDecorator {

  @Override
  public void decorate() {
    CommonParams<?, ?, ?, ?> parameters = commonParamsGet();
    if (parameters == null) {
      return;
    }
    ResultFilter<?, ?> resultFilter = parameters.getFilter();
    ResultFilterDefaults defaults = parameters.getFilterDefaults();
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    if (resultFilter != null) {
      if (resultFilter.getSize() >= 0) {
        cb.setMaxResults(resultFilter.getSize());
      }
      if (resultFilter.getFrom() >= 0) {
        cb.setFirstResult(resultFilter.getFrom());
      }
    } else if (defaults != null) {
      if (defaults.getSize() >= 0) {
        cb.setMaxResults(defaults.getSize());
      }
      if (defaults.getFrom() >= 0) {
        cb.setFirstResult(defaults.getFrom());
      }
    }
  }
}
