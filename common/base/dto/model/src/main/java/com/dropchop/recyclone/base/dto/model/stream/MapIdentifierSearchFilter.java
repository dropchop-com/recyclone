package com.dropchop.recyclone.base.dto.model.stream;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.invoke.Params;

import java.util.Map.Entry;
import java.util.function.Predicate;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 08. 22.
 */
@SuppressWarnings("unused")
public class MapIdentifierSearchFilter<X extends Entry<String, T>, T extends Model>
  extends IdentifierSearch<T> implements Predicate<X> {

  public MapIdentifierSearchFilter(Params params) {
    super(params);
  }

  @Override
  public boolean test(X x) {
    return super.test(x.getValue());
  }
}
