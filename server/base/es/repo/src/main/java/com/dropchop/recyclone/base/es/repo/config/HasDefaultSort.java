package com.dropchop.recyclone.base.es.repo.config;

import com.dropchop.recyclone.base.es.model.query.QueryObject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 5/20/25.
 */
public interface HasDefaultSort {

  QueryObject getSortOrder();
}
