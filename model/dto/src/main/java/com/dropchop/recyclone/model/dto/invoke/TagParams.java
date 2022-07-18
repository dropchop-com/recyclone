package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.model.api.base.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 06. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class TagParams extends TypeParams {

  @Override
  @JsonIgnore
  public Collection<State.Code> getHiddenStates() {
    return List.of(State.Code.deactivated);
  }

  @Singular
  private List<String> types = new ArrayList<>();
}
