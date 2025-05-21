package com.dropchop.recyclone.base.dto.model.localization;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
@SuppressWarnings("unused")
public class TitleDescriptionTranslated
  extends TitleTranslated
  implements com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslated {

  public TitleDescriptionTranslated(@NonNull String id, @NonNull String lang, @NonNull String title) {
    super(id, lang, title);
  }

  private String description;
}
