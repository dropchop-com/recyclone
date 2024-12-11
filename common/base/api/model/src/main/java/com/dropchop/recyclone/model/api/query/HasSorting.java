package com.dropchop.recyclone.model.api.query;
import java.util.List;

public interface HasSorting {
  List<SortCriterion> getSortCriteria();
  void setSortCriteria(List<SortCriterion> sortCriteria);
}