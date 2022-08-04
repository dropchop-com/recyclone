package com.dropchop.recyclone.model.dto.tagging;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@JsonInclude(NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
public class Tag<TT extends TitleTranslation>
  extends DtoId implements com.dropchop.recyclone.model.api.tagging.Tag<Tag<TT>, TT> {

  private final String type = this.getClass().getSimpleName();

  private String title;

  private String lang;

  private Set<TT> translations;

  private List<Tag<TT>> tags;

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
