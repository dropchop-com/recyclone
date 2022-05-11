package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.model.api.invoke.Params;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 03. 22.
 */
public class PageCriteriaDecorator<T, P extends Params> extends BlazeCriteriaDecorator<T, P> {

  @Override
  public void decorate() {
    P parameters = getContext().getParams();
    CriteriaBuilder<T> cb = getContext().getCriteriaBuilder();
    if (parameters.getSize() >= 0) {
      cb.setMaxResults(parameters.getSize());
    }
    if (parameters.getFrom() >= 0) {
      cb.setFirstResult(parameters.getFrom());
    }
  }
}
