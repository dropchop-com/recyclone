package com.dropchop.recyclone.repo.es.listener;

import java.util.HashMap;

public interface QuerySearchResultListener<T> {
  <S> void onResult(S result);
}
