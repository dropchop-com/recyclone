package com.dropchop.recyclone.base.api.model.query;

import com.dropchop.recyclone.base.api.model.query.operator.filter.Filter;

public interface HasFiltering {
  Filter getFilter();
  void setFilter(Filter filter);
}
