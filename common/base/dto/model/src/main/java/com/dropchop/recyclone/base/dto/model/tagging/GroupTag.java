package com.dropchop.recyclone.base.dto.model.tagging;


import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 04. 25.
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
@SuppressWarnings("unused")
public class GroupTag extends NamedTag
    implements com.dropchop.recyclone.base.api.model.tagging.GroupTag<Tag, TitleDescriptionTranslation> {

  public GroupTag(@NonNull String name) {
    super(name);
  }
}
