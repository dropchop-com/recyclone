package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.WhereOrBuilder;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.dto.invoke.TypeParams;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 07. 22.
 */
public class TypeCriteriaDecorator extends BlazeCriteriaDecorator {

  @Override
  public void decorate() {
    Params params = getContext().getParams();
    List<String> types;

    String alias = getContext().getRootAlias();
    String typeColName = alias + ".type";

    if (params instanceof TypeParams) {
      types = ((TypeParams) params).getTypes();
    } else {
      return;
    }

    if (types.isEmpty()) {
      return;
    }

    boolean enableLike = false;
    for (String type : types) {
      if (type.endsWith("*")) {
        enableLike = true;
        break;
      }
    }

    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    if (!enableLike) {
      cb.where(typeColName).in(types);
    } else {
      WhereOrBuilder<? extends CriteriaBuilder<?>> wob = cb.whereOr();
      for (String type : types) {
        wob.where(typeColName).like().value(type.replace("*", "%").replace("?", "_")).noEscape();
      }
      wob.endOr();
    }
  }
}
