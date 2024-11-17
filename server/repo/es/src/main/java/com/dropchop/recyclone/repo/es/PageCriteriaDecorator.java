package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.model.api.invoke.CommonParams;
import com.dropchop.recyclone.model.api.invoke.ResultFilter;
import com.dropchop.recyclone.model.api.invoke.ResultFilterDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PageCriteriaDecorator extends ElasticCriteriaDecorator {

  @Override
  public void decorate() {
    CommonParams<?, ?, ?, ?> parameters = commonParamsGet();
    ElasticExecContext<?> context = getContext();
    ResultFilter<?, ?> resultFilter = parameters.getFilter();
    ResultFilterDefaults defaults = parameters.getFilterDefaults();

    if (resultFilter != null) {
      applyPagination(context, resultFilter.getFrom(), resultFilter.getSize());
    } else if (defaults != null) {
      applyPagination(context, defaults.getFrom(), defaults.getSize());
    }
  }

  private void applyPagination(ElasticExecContext<?> context, int from, int size) {
    context.getQueryParams().getFilter().setFrom(from);
    context.getQueryParams().getFilter().setSize(size);
  }
}
