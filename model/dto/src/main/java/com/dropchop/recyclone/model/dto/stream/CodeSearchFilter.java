package com.dropchop.recyclone.model.dto.stream;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.dto.base.DtoCode;

import java.util.function.Predicate;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 08. 22.
 */
public class CodeSearchFilter<T extends DtoCode>
  extends CodeSearch<T>
  implements Predicate<T> {

  public CodeSearchFilter(Params params) {
    super(params);
  }

  @Override
  public boolean test(T t) {
    return super.test(t);
  }
}
