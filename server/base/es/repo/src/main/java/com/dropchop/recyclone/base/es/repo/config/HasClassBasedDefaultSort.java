package com.dropchop.recyclone.base.es.repo.config;

import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasPublished;
import com.dropchop.recyclone.base.es.model.query.Sort;

import static com.dropchop.recyclone.base.es.model.query.SortField.Order.DESC;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/26/25.
 */
public interface HasClassBasedDefaultSort extends HasDefaultSort, ClassIndexConfig, HasSearchAfterTieBreaker {

  default void appendTieBreaker(Sort defaultSort, Class<?> rootClass) {
    if (HasUuid.class.isAssignableFrom(rootClass)) {
      defaultSort.addSort("uuid", DESC);
    }
    if (HasCode.class.isAssignableFrom(rootClass)) {
      defaultSort.addSort("code", DESC);
    }
    if (HasId.class.isAssignableFrom(rootClass)) {
      defaultSort.addSort("id", DESC);
    }
  }

  default Sort getSortOrder() {
    Sort defaultSort = new Sort(null);
    Class<?> rootClass = getRootClass();
    if (HasCreated.class.isAssignableFrom(rootClass)) {
      defaultSort.addSort("created", DESC);
      appendTieBreaker(defaultSort, rootClass);
    } else if (HasModified.class.isAssignableFrom(rootClass)) {
      defaultSort.put("modified", DESC);
      appendTieBreaker(defaultSort, rootClass);
    } else if (HasPublished.class.isAssignableFrom(rootClass)) {
      defaultSort.put("published", DESC);
      appendTieBreaker(defaultSort, rootClass);
    } else if (HasUuid.class.isAssignableFrom(rootClass)) {
      defaultSort.put("uuid", DESC);
    } else if (HasCode.class.isAssignableFrom(rootClass)) {
      defaultSort.put("code", DESC);
    } else if (HasId.class.isAssignableFrom(rootClass)) {
      defaultSort.put("id", DESC);
    } else {
      return null;
    }
    return defaultSort;
  }
}
