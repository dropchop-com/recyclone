package com.dropchop.recyclone.repo.api.listener;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 5. 12. 24
 **/
public interface MapQuerySearchResultListener extends QuerySearchResultListener {
  <S> void onResult(S result);
}
