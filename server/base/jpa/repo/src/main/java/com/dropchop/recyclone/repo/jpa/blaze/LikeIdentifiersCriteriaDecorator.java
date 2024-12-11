package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.model.api.invoke.CodeParams;
import com.dropchop.recyclone.model.api.invoke.IdentifierParams;
import com.dropchop.recyclone.model.api.invoke.Params;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 03. 22.
 */
public class LikeIdentifiersCriteriaDecorator extends LikeListCriteriaDecorator {

  @Override
  public void decorate() {
    Params params = getContext().getParams();
    List<String> ids;

    String alias = getContext().getRootAlias();
    String idColName = alias + ".uuid";
    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    if (params instanceof IdentifierParams) {
      ids = ((IdentifierParams<?, ?, ?, ?>) params).getIdentifiers();
      if (!ids.isEmpty()) {
        cb.where(idColName).in(ids.stream().map(UUID::fromString).collect(Collectors.toList()));
      }
    } else if (params instanceof CodeParams) {
      ids = ((CodeParams<?, ?, ?, ?>) params).getCodes();
      idColName = alias + ".code";
      if (!ids.isEmpty()) {
        decorateWithListOfStringsAsLike(ids, idColName);
      }
    }
  }
}
