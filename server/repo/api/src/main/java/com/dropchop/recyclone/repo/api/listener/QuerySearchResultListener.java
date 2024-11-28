package com.dropchop.recyclone.repo.api.listener;

public interface QuerySearchResultListener {
  <S> void onResult(S result);
}
