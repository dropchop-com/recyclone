package com.dropchop.recyclone.base.dto.model.localization;

import com.dropchop.recyclone.base.dto.model.base.Generic;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
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

  private String description;

  public TitleDescriptionTranslated(
      com.dropchop.recyclone.base.api.model.localization.TitleTranslated titleTranslated) {
    this.setId(titleTranslated.getId());
    this.setLang(titleTranslated.getLang());
    this.setTitle(titleTranslated.getTitle());
    this.setType(titleTranslated.getType());
    if (titleTranslated instanceof Generic generic) {
      this.setName(generic.getName());
    }
    if (!titleTranslated.getGroups().isEmpty()) {
      this.setGroups(titleTranslated.getGroups());
    }
  }
}
