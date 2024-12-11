package com.dropchop.recyclone.model.api.query;

public interface HasPagination {
  int getPage();
  void setPage(int page);

  int getSize();
  void setSize(int size);
}
