package com.dropchop.recyclone.base.api.repo.config;

import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasPublished;
import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/26/25.
 */
public interface HasClassBasedDefaultSort extends ClassIndexConfig {
  default QueryNodeObject getSortOrder() {
    QueryNodeObject defaultSort = new QueryNodeObject();
    Class<?> rootClass = getRootClass();
    if (HasCreated.class.isAssignableFrom(rootClass)) {
      defaultSort.put("created", "desc");
    } else if (HasModified.class.isAssignableFrom(rootClass)) {
      defaultSort.put("modified", "desc");
    } else if (HasPublished.class.isAssignableFrom(rootClass)) {
      defaultSort.put("published", "desc");
    } else if (HasUuid.class.isAssignableFrom(rootClass)) {
      defaultSort.put("uuid", "desc");
    } else if (HasCode.class.isAssignableFrom(rootClass)) {
      defaultSort.put("code", "desc");
    } else if (HasId.class.isAssignableFrom(rootClass)) {
      defaultSort.put("id", "desc");
    } else {
      return null;
    }
    return defaultSort;
  }
}
