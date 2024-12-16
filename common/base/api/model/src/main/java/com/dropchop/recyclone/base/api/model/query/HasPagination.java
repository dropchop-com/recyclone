package com.dropchop.recyclone.base.api.model.query;

public interface HasPagination {
  int getPage();
  void setPage(int page);

  int getSize();
  void setSize(int size);
}
