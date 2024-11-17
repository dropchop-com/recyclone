package com.dropchop.recyclone.model.api.query;

public interface HasFiltering {
  FilterCriteria getFilterCriteria();
  void setFilterCriteria(FilterCriteria filterCriteria);
}
