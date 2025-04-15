package com.dropchop.recyclone.base.es.model.localization;

import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;
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
public class EsTitleDescriptionTranslation extends EsTitleTranslation
  implements TitleDescriptionTranslation {

  public EsTitleDescriptionTranslation(@NonNull String lang, @NonNull String title) {
    super(lang, title);
  }

  private String description;
}
