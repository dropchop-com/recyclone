package com.dropchop.recyclone.base.dto.model.invoke;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

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

  /**
   * Custom @Singular implementation.
   */
  @SuppressWarnings("unused")
  public abstract static class TypeParamsBuilder<C extends TypeParams, B extends TypeParams.TypeParamsBuilder<C, B>>
      extends IdentifierParams.IdentifierParamsBuilder<C, B> {
    public B type(String code) {
      if (this.types$value == null) {
        this.types$value = new ArrayList<>();
      }
      this.types$value.add(code);
      this.types$set = true;
      return self();
    }
  }

  @Builder.Default
  @JsonInclude(NON_EMPTY)
  private List<String> types = new ArrayList<>();

  @Override
  public String toString() {
    return super.toString() + ",types:" + getTypes();
  }
}
