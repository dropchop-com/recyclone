package com.dropchop.recyclone.base.dto.model.stream;

import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.dto.model.base.DtoCode;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 08. 22.
 */
public class CodeSearch<T extends DtoCode> {

  private final List<String> searchCodes;

  public CodeSearch(Params params) {
    if (params instanceof CodeParams codeParams) {
      List<String> searchCodes = codeParams.getCodes();
      this.searchCodes = Objects.requireNonNullElse(searchCodes, Collections.emptyList());
    } else {
      this.searchCodes = Collections.emptyList();
    }
  }

  public boolean test(T t) {
    if (t == null) {
      return false;
    }
    String code = t.getCode();
    if (code.isBlank()) {
      return false;
    }
    for (String searchCode : searchCodes) {
      if (
        (searchCode.equals(code))
          ||
          (searchCode.endsWith("*") && code.startsWith(searchCode.substring(0, searchCode.length() - 1)))
      ) {
        return true;
      }
    }
    return searchCodes.isEmpty();
  }
}
