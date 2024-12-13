package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.base.api.model.base.State;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 06. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@SuppressWarnings("unused")
public class TagParams extends TypeParams {

  public static class Defaults extends ResultFilterDefaults {
    @Override
    public Collection<State.Code> getAvailableHiddenStates() {
      return Set.of(HasDeactivated.deactivated);
    }

    @Override
    public String[] getAvailableSortFields() {
      return new String[] {
          //TODO: "+name", "name", "-name",
          "+created", "created", "-created",
          "+type", "type", "-type"
      };
    }
  }

  @Override
  @JsonIgnore
  public ResultFilterDefaults getFilterDefaults() {
    return new Defaults();
  }
}
