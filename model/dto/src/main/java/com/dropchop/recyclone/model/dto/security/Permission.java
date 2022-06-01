package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Permission extends DtoId
  implements com.dropchop.recyclone.model.api.security.Permission<TitleTranslation, Action, Domain> {

  private Domain domain;

  private Action action;

  private List<String> instances;

  @JsonInclude(NON_NULL)
  private String title;

  @JsonInclude(NON_NULL)
  private String lang;

  @JsonInclude(NON_NULL)
  private Set<TitleTranslation> translations;
}
