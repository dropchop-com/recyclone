package com.dropchop.recyclone.model.dto.tagging;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(NON_NULL)
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type"
)
public class Tag
  extends DtoId implements com.dropchop.recyclone.model.api.tagging.Tag<Tag, TitleDescriptionTranslation> {

  private final String type = this.getClass().getSimpleName();

  private String title;

  private String description;

  private String lang;

  @Singular
  @JsonInclude(NON_EMPTY)
  private Set<TitleDescriptionTranslation> translations;

  @Singular
  @JsonInclude(NON_EMPTY)
  private List<Tag> tags;

  @Singular
  @JsonInclude(NON_EMPTY)
  private Set<Attribute<?>> attributes;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
  }
}
