package com.dropchop.recyclone.model.dto.tagging;

import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type"
)
public class LanguageGroup extends NamedTag
  implements com.dropchop.recyclone.model.api.tagging.LanguageGroup<Tag, TitleDescriptionTranslation> {

  public LanguageGroup(@NonNull String name) {
    super(name);
  }
}
