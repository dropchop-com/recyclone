package com.dropchop.recyclone.repo.api.listener;

import com.dropchop.recyclone.mapper.api.MappingListener;

public interface QuerySearchResultListener extends MappingListener {
  <S> void onResult(S result);
}
