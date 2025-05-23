package com.dropchop.recyclone.base.jpa.repo;

import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.dto.model.invoke.TypeParams;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 07. 22.
 */
public class LikeTypeCriteriaDecorator<E> extends LikeListCriteriaDecorator<E> {

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

    decorateWithListOfStringsAsLike(types, typeColName);
  }
}
