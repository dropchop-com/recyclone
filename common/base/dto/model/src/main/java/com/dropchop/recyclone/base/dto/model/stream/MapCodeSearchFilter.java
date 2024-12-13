package com.dropchop.recyclone.base.dto.model.stream;

import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.dto.model.base.DtoCode;

import java.util.Map.Entry;
import java.util.function.Predicate;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 08. 22.
 */
@SuppressWarnings("unused")
public class MapCodeSearchFilter<X extends Entry<String, T>, T extends DtoCode>
  extends CodeSearch<T> implements Predicate<X> {

  public MapCodeSearchFilter(Params params) {
    super(params);
  }

  @Override
  public boolean test(X x) {
    return super.test(x.getValue());
  }
}
