package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.base.State;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 06. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@JsonInclude(NON_EMPTY)
public class TagParams extends TypeParams {

  /**
   * Custom @Singular implementation.
   */
  @SuppressWarnings("unused")
  public abstract static class TagParamsBuilder<C extends TagParams, B extends TagParams.TagParamsBuilder<C, B>>
      extends TypeParams.TypeParamsBuilder<C, B> {
    public B tag(Tag tag) {
      if (this.tags$value == null) {
        this.tags$value = new ArrayList<>();
      }
      this.tags$value.add(tag);
      this.tags$set = true;
      return self();
    }
  }

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

  @Builder.Default
  private List<Tag> tags = new ArrayList<>();

}
