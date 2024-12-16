package com.dropchop.recyclone.base.api.model.query;
import java.util.List;

public interface HasSorting {
  List<SortCriterion> getSortCriteria();
  void setSortCriteria(List<SortCriterion> sortCriteria);
}