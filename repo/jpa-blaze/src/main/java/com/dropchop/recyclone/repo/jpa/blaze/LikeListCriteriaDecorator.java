package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.WhereOrBuilder;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 03. 22.
 */
public abstract class LikeListCriteriaDecorator extends BlazeCriteriaDecorator {

  protected void decorateWithListOfStringsAsLike(List<String> strings, String colName) {
    if (strings.isEmpty()) {
      return;
    }

    boolean enableLike = false;
    for (String s : strings) {
      if (s.endsWith("*")) {
        enableLike = true;
        break;
      }
    }

    CriteriaBuilder<?> cb = getContext().getCriteriaBuilder();
    if (!enableLike) {
      cb.where(colName).in(strings);
    } else {
      WhereOrBuilder<? extends CriteriaBuilder<?>> wob = cb.whereOr();
      for (String s : strings) {
        wob.where(colName).like().value(s.replace("*", "%").replace("?", "_")).noEscape();
      }
      wob.endOr();
    }
  }
}
