package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.CommonParams;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Params implements
    Dto,
    CommonParams<ResultFilter, ResultFilter.ContentFilter, ResultFilter.LanguageFilter, ResultFilterDefaults> {

  private String requestId;

  @Builder.Default
  private ResultFilter filter = new ResultFilter();

  @Override
  @JsonIgnore
  public ResultFilterDefaults getFilterDefaults() {
    return new ResultFilterDefaults();
  }

  @Singular
  @JsonInclude(NON_EMPTY)
  private Set<Attribute<?>> attributes = new LinkedHashSet<>();

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":" + getRequestId();
  }
}
