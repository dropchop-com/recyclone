package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.model.api.base.State;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class CodeParams extends Params
  implements com.dropchop.recyclone.model.api.invoke.CodeParams {

  @Override
  @JsonIgnore
  public Collection<State.Code> getHiddenStates() {
    return Set.of(HasDeactivated.deactivated);
  }

  @Override
  @JsonIgnore
  public String[] getSortFields() {
    return new String[] {"+code", "code", "-code"};
  }

  @ToString.Include
  @Singular
  private List<String> codes = new ArrayList<>();
}
