package com.dropchop.recyclone.base.api.service;

import com.dropchop.recyclone.base.api.model.base.Model;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
public interface ByIdService<M extends Model, ID> extends Service {

  Class<M> getRootClass();

  M findById(ID id);

  List<M> findById(Collection<ID> ids);

  List<M> find();
}
