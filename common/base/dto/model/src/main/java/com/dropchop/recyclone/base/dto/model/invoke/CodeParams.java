package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.base.State;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.ContentFilter;
import com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.LanguageFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class CodeParams extends Params
  implements com.dropchop.recyclone.base.api.model.invoke.CodeParams<
    ResultFilter,
    ContentFilter,
    LanguageFilter,
    ResultFilterDefaults> {

  public static class Defaults extends ResultFilterDefaults {
    @Override
    public Collection<State.Code> getAvailableHiddenStates() {
      return Set.of(HasDeactivated.deactivated);
    }

    @Override
    public String[] getAvailableSortFields() {
      return new String[] {"+code", "code", "-code"};
    }
  }

  @Override
  @JsonIgnore
  public ResultFilterDefaults getFilterDefaults() {
    return new Defaults();
  }

  @ToString.Include
  @Singular
  private List<String> codes = new ArrayList<>();

  @Override
  public String toString() {
    return super.toString() + ":" + getCodes();
  }
}
