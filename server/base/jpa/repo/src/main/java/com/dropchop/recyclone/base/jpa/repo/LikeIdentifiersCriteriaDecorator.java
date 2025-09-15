package com.dropchop.recyclone.base.jpa.repo;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.base.api.model.invoke.CodeParams;
import com.dropchop.recyclone.base.api.model.invoke.IdentifierParams;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 03. 22.
 */
public class LikeIdentifiersCriteriaDecorator<E> extends LikeListCriteriaDecorator<E> {

  @Override
  public void decorate() {
    Params params = getContext().getParams();
    List<String> ids;

    String alias = getContext().getRootAlias();
    Class<?> clazz = getContext().getRootClass();
    String idColName = alias + ".uuid";
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    if (params instanceof IdentifierParams && HasUuid.class.isAssignableFrom(clazz)) {
      ids = ((IdentifierParams<?, ?, ?, ?>) params).getIdentifiers();
      if (!ids.isEmpty()) {
        cb.where(idColName).in(ids.stream().map(UUID::fromString).collect(Collectors.toList()));
      }
    } else if (params instanceof CodeParams && HasCode.class.isAssignableFrom(clazz)) {
      ids = ((CodeParams<?, ?, ?, ?>) params).getCodes();
      idColName = alias + ".code";
      if (!ids.isEmpty()) {
        decorateWithListOfStringsAsLike(ids, idColName);
      }
    }
  }
}
