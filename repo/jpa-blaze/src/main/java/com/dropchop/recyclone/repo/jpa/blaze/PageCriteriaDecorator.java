package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.model.api.invoke.CommonParams;
import com.dropchop.recyclone.model.api.invoke.Params;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 03. 22.
 */
@Slf4j
public class PageCriteriaDecorator<T> extends BlazeCriteriaDecorator<T> {

  @Override
  public void decorate() {
    Params params = getContext().getParams();
    if (!(params instanceof CommonParams parameters)) {
      log.warn("Wrong parameters instance [{}] should be [{}]", params.getClass(), CommonParams.class);
      return;
    }
    CriteriaBuilder<T> cb = getContext().getCriteriaBuilder();
    if (parameters.getSize() >= 0) {
      cb.setMaxResults(parameters.getSize());
    }
    if (parameters.getFrom() >= 0) {
      cb.setFirstResult(parameters.getFrom());
    }
  }
}
