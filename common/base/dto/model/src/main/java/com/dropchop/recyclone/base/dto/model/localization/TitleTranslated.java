package com.dropchop.recyclone.base.dto.model.localization;

import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.dto.model.base.Generic;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 5. 2025.
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class TitleTranslated extends Generic
    implements HasId, com.dropchop.recyclone.base.api.model.localization.TitleTranslated {

  @NonNull
  private String lang;

  @NonNull
  @EqualsAndHashCode.Exclude
  private String title;

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":" + this.getId() + ":" + this.getLang() + ",t:" + this.getTitle();
  }
}
