package com.dropchop.recyclone.base.dto.model.invoke;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class IdentifierParams extends Params
  implements com.dropchop.recyclone.base.api.model.invoke.IdentifierParams<
    ResultFilter,
    ResultFilter.ContentFilter,
    ResultFilter.LanguageFilter,
    ResultFilterDefaults> {

  @Singular
  @ToString.Include
  private List<String> identifiers = new ArrayList<>();

  @Override
  public String toString() {
    return super.toString() + ":" + getIdentifiers();
  }
}
