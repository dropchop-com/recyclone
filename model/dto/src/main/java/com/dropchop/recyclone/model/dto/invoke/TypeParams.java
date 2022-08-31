package com.dropchop.recyclone.model.dto.invoke;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 06. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class TypeParams extends IdentifierParams
  implements com.dropchop.recyclone.model.api.invoke.TypeParams<
  ResultFilter,
  ResultFilter.ContentFilter,
  ResultFilter.LanguageFilter,
  ResultFilterDefaults> {
  private List<String> types;
}
