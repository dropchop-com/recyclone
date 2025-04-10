package com.dropchop.recyclone.base.es.repo.config;

import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/25/25.
 */
public interface HasQueryBasedReadIndex {
  String getReadIndexName(QueryNodeObject query);
}
