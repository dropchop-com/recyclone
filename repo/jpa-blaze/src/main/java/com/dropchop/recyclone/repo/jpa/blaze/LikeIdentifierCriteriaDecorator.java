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
public class LikeIdentifierCriteriaDecorator extends BlazeCriteriaDecorator {

  @Override
  public void decorate() {
    Params params = getContext().getParams();
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

    boolean enableLike = false;
    for (String id : ids) {
      if (id.endsWith("*")) {
        enableLike = true;
        break;
      }
    }

    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    if (!enableLike) {
      cb.where(idColName).in(ids);
    } else {
      WhereOrBuilder<? extends CriteriaBuilder<?>> wob = cb.whereOr();
      for (String id : ids) {
        wob.where(idColName).like().value(id.replace("*", "%").replace("?", "_")).noEscape();
      }
      wob.endOr();
    }
  }
}
