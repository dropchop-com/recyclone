package com.dropchop.recyclone.model.dto.stream;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 08. 22.
 */
public class IdentifierSearch<T extends Model> {

  private final List<String> searches;

  public IdentifierSearch(Params params) {
    if (params instanceof CodeParams codeParams) {
      List<String> searchCodes = codeParams.getCodes();
      this.searches = Objects.requireNonNullElse(searchCodes, Collections.emptyList());
    } else if (params instanceof IdentifierParams identifierParams) {
      List<String> searchCodes = identifierParams.getIdentifiers();
      this.searches = Objects.requireNonNullElse(searchCodes, Collections.emptyList());
    } else {
      this.searches = Collections.emptyList();
    }
  }

  public boolean test(T t) {
    if (t == null) {
      return false;
    }
    String id = t.identifier();
    if (id.isBlank()) {
      return false;
    }
    for (String search : searches) {
      if (
        (search.equals(id))
          ||
          (search.endsWith("*") && id.startsWith(search.substring(0, search.length() - 1)))
      ) {
        return true;
      }
    }
    return searches.isEmpty();
  }
}
