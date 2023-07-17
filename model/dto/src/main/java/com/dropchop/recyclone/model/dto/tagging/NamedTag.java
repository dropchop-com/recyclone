package com.dropchop.recyclone.model.dto.tagging;

import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type"
)
public class NamedTag extends Tag
  implements com.dropchop.recyclone.model.api.tagging.NamedTag<Tag, TitleDescriptionTranslation> {

  @NonNull
  private String name;

  @Override
  public String toString() {
    return super.toString() + ",n:" + getName();
  }
}
