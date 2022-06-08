package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@JsonInclude(NON_NULL)
public class Permission extends DtoId
  implements com.dropchop.recyclone.model.api.security.Permission<TitleTranslation, Action, Domain>,
  Comparable<Permission> {

  private Domain domain;

  private Action action;

  private List<String> instances;

  private String title;

  private String lang;

  private Set<TitleTranslation> translations;

  @JsonIgnore
  private String getDescriptor() {
    String code = this.domain != null ? this.domain.getCode() : "";
    code += this.action != null ? this.action.getCode() : "";
    code += this.getUuid();
    return code;
  }

  @Override
  public int compareTo(Permission permission) {
    String my = this.getDescriptor();
    String other = permission.getDescriptor();
    return my.compareTo(other);
  }
}
