package com.dropchop.recyclone.repo.jpa.blaze;

import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.model.dto.invoke.TypeParams;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 07. 22.
 */
public class LikeTypeCriteriaDecorator extends LikeListCriteriaDecorator {

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
