package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.WhereOrBuilder;
import com.dropchop.recyclone.model.api.invoke.CodeParams;
import com.dropchop.recyclone.model.api.invoke.IdentifierParams;
import com.dropchop.recyclone.model.api.invoke.Params;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 03. 22.
 */
public class LikeIdentifierCriteriaDecorator<T, P extends Params> extends BlazeCriteriaDecorator<T, P> {

  @Override
  public void decorate() {
    P params = getContext().getParams();
    List<String> ids;

    String alias = getContext().getRootAlias();
    String idColName = alias + ".uuid";

    if (params instanceof IdentifierParams) {
      ids = ((IdentifierParams) params).getIdentifiers();
    } else if (params instanceof CodeParams) {
      ids = ((CodeParams) params).getCodes();
      idColName = alias + ".code";
    } else {
      return;
    }

    if (ids.isEmpty()) {
      return;
    }

    CriteriaBuilder<T> cb = getContext().getCriteriaBuilder();
    boolean enableLike = false;
    for (String id : ids) {
      if (id.endsWith("*")) {
        enableLike = true;
        break;
      }
    }

    if (!enableLike) {
      cb.where(idColName).in(ids);
    } else {
      WhereOrBuilder<CriteriaBuilder<T>> wob = cb.whereOr();
      for (String id : ids) {
        wob.where(idColName).like().value(id.replace("*", "%").replace("?", "_")).noEscape();
      }
      wob.endOr();
    }
  }
}
