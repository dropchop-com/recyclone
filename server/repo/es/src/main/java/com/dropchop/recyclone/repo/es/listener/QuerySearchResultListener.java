package com.dropchop.recyclone.repo.es.listener;

public interface QuerySearchResultListener {
  <S> void onResult(S result);
}
