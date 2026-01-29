package com.dropchop.recyclone.base.es.repo.config;

import com.dropchop.recyclone.base.es.model.query.IQueryObject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 5/20/25.
 */
public interface HasDefaultSort {

  IQueryObject getSortOrder();
}
