package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.CommonParams;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class Params implements Dto, CommonParams<ResultFilter, ResultFilter.ContentFilter, ResultFilter.LanguageFilter, ResultFilterDefaults> {

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
}
