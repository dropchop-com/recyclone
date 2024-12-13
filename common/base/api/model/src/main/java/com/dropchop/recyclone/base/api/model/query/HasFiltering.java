package com.dropchop.recyclone.base.api.model.query;

public interface HasFiltering {
  FilterCriteria getFilterCriteria();
  void setFilterCriteria(FilterCriteria filterCriteria);
}
