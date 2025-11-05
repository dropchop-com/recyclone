package com.dropchop.recyclone.base.dto.model.localization;

import com.dropchop.recyclone.base.api.model.base.Generic;
import com.dropchop.recyclone.base.api.model.marker.HasDescription;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.HasLanguageCode;
import com.dropchop.recyclone.base.api.model.marker.HasName;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.dto.model.rest.AggregationResult;
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
    this.setDeactivated(titleTranslated.getDeactivated());
    this.setModified(titleTranslated.getModified());
    this.setType(titleTranslated.getType());
    if (titleTranslated instanceof HasName named) {
      this.setName(named.getName());
    }
    if (!titleTranslated.getGroups().isEmpty()) {
      this.setGroups(titleTranslated.getGroups());
    }
  }

  public TitleDescriptionTranslated(
      com.dropchop.recyclone.base.api.model.base.Titled titled) {
    this.setTitle(titled.getTitle());
    if (titled instanceof HasName named) {
      this.setName(named.getName());
    }
    if (titled instanceof HasId identified) {
      this.setId(identified.getId());
    }
    if (titled instanceof HasDescription hasDescription) {
      this.setDescription(hasDescription.getDescription());
    }
    if (titled instanceof HasLanguageCode hasLanguageCode) {
      this.setLang(hasLanguageCode.getLang());
    }
    if (titled instanceof HasModified hasModified) {
      this.setModified(hasModified.getModified());
    }
    if (titled instanceof HasDeactivated hasDeactivated) {
      this.setDeactivated(hasDeactivated.getDeactivated());
    }
    if (titled instanceof HasLanguageCode hasLanguageCode) {
      this.setLang(hasLanguageCode.getLang());
    }
    if (titled instanceof Generic generic) {
      this.setType(generic.getType());
      if (!generic.getGroups().isEmpty()) {
        this.setGroups(generic.getGroups());
      }
    }
  }

  public TitleDescriptionTranslated(AggregationResult.Container container) {
    if (container.getId() != null) {
      this.setId(container.getId());
    }
    if (container.getLang() != null) {
      this.setLang(container.getLang());
    }
    if (container.getType() != null) {
      this.setType(container.getType());
    }
    if (container.getTitle() != null) {
      this.setTitle(container.getTitle());
    }
  }
}
