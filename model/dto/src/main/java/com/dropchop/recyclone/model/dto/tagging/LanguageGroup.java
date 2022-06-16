package com.dropchop.recyclone.model.dto.tagging;

import com.dropchop.recyclone.model.api.tagging.NamedTag;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@JsonInclude(NON_NULL)
public class LanguageGroup extends Tag<TitleTranslation> implements NamedTag<TitleTranslation> {
  @NonNull
  private String name;
}
