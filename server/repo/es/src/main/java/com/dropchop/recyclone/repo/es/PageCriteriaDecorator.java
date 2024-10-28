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
    if (parameters == null) {
      log.warn("Parameters are missing in the execution context.");
      return;
    }

    ElasticExecContext<?> context = getContext();
    String query = context.getQuery();

    ResultFilter<?, ?> resultFilter = parameters.getFilter();
    ResultFilterDefaults defaults = parameters.getFilterDefaults();

    if (resultFilter != null) {
      query = applyPagination(query, resultFilter.getFrom(), resultFilter.getSize());
    } else if (defaults != null) {
      query = applyPagination(query, defaults.getFrom(), defaults.getSize());
    }

    context.setQuery(query);
    log.debug("Updated query with pagination: {}", query);
  }

  private String applyPagination(String query, int from, int size) {
    if (query == null || query.trim().isEmpty()) {
      query = "{}"; // Default query if none is provided
    }

    StringBuilder sb = new StringBuilder(query.trim());
    if (!sb.toString().endsWith("}")) {
      sb.append("}");
    }

    String paginationJson = String.format(", \"from\": %d, \"size\": %d", from, size);
    int lastIndex = sb.lastIndexOf("}");
    sb.insert(lastIndex, paginationJson);

    return sb.toString();
  }
}
