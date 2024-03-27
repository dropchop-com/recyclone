package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Model;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
public interface ByIdService<M extends Model, ID> extends Service {

  Class<M> getRootClass();

  M findById(ID id);

  List<M> findById(Collection<ID> ids);

  List<M> find();
}
