package com.dropchop.recyclone.base.dto.model.stream;

import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.dto.model.base.DtoCode;

import java.util.function.Predicate;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 08. 22.
 */
@SuppressWarnings("unused")
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
