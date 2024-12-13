package com.dropchop.recyclone.model.dto.localization;

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
public class TitleTranslation implements com.dropchop.recyclone.base.api.model.localization.TitleTranslation {

  @NonNull
  private String lang;

  @NonNull
  @EqualsAndHashCode.Exclude
  private String title;

  @EqualsAndHashCode.Exclude
  private Boolean base;

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":" + this.getLang() + ",t:" + this.getTitle();
  }
}
