package com.dropchop.recyclone.model.dto.invoke;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 06. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TypeParams extends IdentifierParams
  implements com.dropchop.recyclone.base.api.model.invoke.TypeParams<
    ResultFilter,
    ResultFilter.ContentFilter,
    ResultFilter.LanguageFilter,
    ResultFilterDefaults> {

  @Singular
  private List<String> types = new ArrayList<>();

  @Override
  public String toString() {
    return super.toString() + ",types:" + getTypes();
  }
}
