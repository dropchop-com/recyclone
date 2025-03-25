package com.dropchop.recyclone.base.api.repo.config;

import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/25/25.
 */
public interface HasQueryBasedReadIndex {
  String getReadIndexName(QueryNodeObject query);
}
