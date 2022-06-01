package com.dropchop.recyclone.model.dto.tag;

import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class Tag<T extends TitleTranslation>
  extends DtoId implements com.dropchop.recyclone.model.api.tag.Tag<T> {

  @JsonInclude(NON_NULL)
  private String title;

  @JsonInclude(NON_NULL)
  private String lang;

  @JsonInclude(NON_NULL)
  private Set<T> translations;

  @JsonInclude(NON_NULL)
  private ZonedDateTime created;

  @JsonInclude(NON_NULL)
  private ZonedDateTime modified;

  @JsonInclude(NON_NULL)
  private ZonedDateTime deactivated;

  private final String type = this.getClass().getSimpleName();

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
  }
}
