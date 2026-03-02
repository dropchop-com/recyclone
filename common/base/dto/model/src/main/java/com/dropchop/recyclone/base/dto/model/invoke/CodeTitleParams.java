package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.ContentFilter;
import com.dropchop.recyclone.base.dto.model.invoke.ResultFilter.LanguageFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@JsonInclude(NON_EMPTY)
public class CodeTitleParams extends CodeParams implements com.dropchop.recyclone.base.api.model.invoke.CodeTitleParams<
    ResultFilter,
    ContentFilter,
    LanguageFilter,
    ResultFilterDefaults> {

  String title;

  @Override
  public String toString() {
    return super.toString() + ":" + getCodes() + ":" +  getTitle();
  }
}
