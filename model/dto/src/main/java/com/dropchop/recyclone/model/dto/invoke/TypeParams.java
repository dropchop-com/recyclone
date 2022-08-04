package com.dropchop.recyclone.model.dto.invoke;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 06. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class TypeParams extends IdentifierParams
  implements com.dropchop.recyclone.model.api.invoke.TypeParams {
  private List<String> types;
}
