package com.dropchop.recyclone.repo.api.listener;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;

/**
 * @author Samo Pritrznik <samo.pritrznik@dropchop.com> on 5. 12. 24
 **/
public interface EntityQuerySearchResultListener extends QuerySearchResultListener {
  <E> void onResult(E result);
}
