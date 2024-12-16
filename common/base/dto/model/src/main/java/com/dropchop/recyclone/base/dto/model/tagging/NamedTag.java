package com.dropchop.recyclone.base.dto.model.tagging;

import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type"
)
public class NamedTag extends Tag
  implements com.dropchop.recyclone.base.api.model.tagging.NamedTag<Tag, TitleDescriptionTranslation> {

  @NonNull
  private String name;

  @Override
  public String toString() {
    return super.toString() + ",n:" + getName();
  }
}
