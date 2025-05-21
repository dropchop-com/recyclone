package com.dropchop.recyclone.base.dto.model.localization;

import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
public class TitleTranslated implements HasId, com.dropchop.recyclone.base.api.model.localization.TitleTranslated {

  @NonNull
  private String id;

  @NonNull
  private String lang;

  private String group;

  @NonNull
  @EqualsAndHashCode.Exclude
  private String title;

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":" + this.getId() + ":" + this.getLang() + ",t:" + this.getTitle();
  }
}
