package com.dropchop.recyclone.quarkus.it.model.dto;

import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.dto.model.tagging.NamedTag;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4/18/25.
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
public class DummyTag extends NamedTag
    implements com.dropchop.recyclone.quarkus.it.model.api.DummyTag<Tag, TitleDescriptionTranslation> {

  public DummyTag(@NonNull String name) {
    super(name);
  }
}
