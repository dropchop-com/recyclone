package com.dropchop.recyclone.repo.jpa.blaze;

import com.dropchop.recyclone.model.api.invoke.CodeParams;
import com.dropchop.recyclone.model.api.invoke.IdentifierParams;
import com.dropchop.recyclone.model.api.invoke.Params;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 03. 22.
 */
public class LikeIdentifiersCriteriaDecorator extends LikeListCriteriaDecorator {

  @Override
  public void decorate() {
    Params params = getContext().getParams();
    List<String> ids;

    String alias = getContext().getRootAlias();
    String idColName = alias + ".uuid";

    if (params instanceof IdentifierParams) {
      ids = ((IdentifierParams<?, ?, ?, ?>) params).getIdentifiers();
    } else if (params instanceof CodeParams) {
      ids = ((CodeParams<?, ?, ?, ?>) params).getCodes();
      idColName = alias + ".code";
    } else {
      return;
    }

    decorateWithListOfStringsAsLike(ids, idColName);
  }
}
