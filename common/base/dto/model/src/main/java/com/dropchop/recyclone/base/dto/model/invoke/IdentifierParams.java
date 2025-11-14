package com.dropchop.recyclone.base.dto.model.invoke;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@JsonInclude(NON_EMPTY)
public class IdentifierParams extends Params
  implements com.dropchop.recyclone.base.api.model.invoke.IdentifierParams<
    ResultFilter,
    ResultFilter.ContentFilter,
    ResultFilter.LanguageFilter,
    ResultFilterDefaults> {

  /**
   * Custom @Singular implementation.
   */
  @SuppressWarnings("unused")
  public abstract static class IdentifierParamsBuilder<
      C extends IdentifierParams, B extends IdentifierParams.IdentifierParamsBuilder<C, B>
      >
      extends Params.ParamsBuilder<C, B> {
    public B identifier(String code) {
      if (this.identifiers$value == null) {
        this.identifiers$value = new ArrayList<>();
      }
      this.identifiers$value.add(code);
      this.identifiers$set = true;
      return self();
    }
  }

  @ToString.Include
  @Builder.Default
  private List<String> identifiers = new ArrayList<>();

  @Override
  public String toString() {
    return super.toString() + ":" + getIdentifiers();
  }
}
