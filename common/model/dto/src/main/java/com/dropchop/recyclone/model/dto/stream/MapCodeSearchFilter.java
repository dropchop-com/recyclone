package com.dropchop.recyclone.model.dto.stream;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.dto.base.DtoCode;

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
