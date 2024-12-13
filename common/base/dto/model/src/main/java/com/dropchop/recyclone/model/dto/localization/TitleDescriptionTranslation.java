package com.dropchop.recyclone.model.dto.localization;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
public class TitleDescriptionTranslation
  extends TitleTranslation
  implements com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation {

  public TitleDescriptionTranslation(@NonNull String lang, @NonNull String title) {
    super(lang, title);
  }

  private String description;
}
