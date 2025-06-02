package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.base.dto.model.base.DtoId;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class Permission extends DtoId
  implements com.dropchop.recyclone.base.api.model.security.Permission<TitleDescriptionTranslation, Action, Domain>,
  Comparable<Permission>, HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon {

  private Domain domain;

  private Action action;

  @JsonInclude(NON_EMPTY)
  private List<String> instances;

  private String title;

  private String description;

  private String lang;

  @JsonInclude(NON_EMPTY)
  private Set<TitleDescriptionTranslation> translations;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;

  private String wildcardString;

  public Permission(@NonNull String id) {
    super(id);
  }


  //@JsonIgnore
  public String getWildcardString() {
    StringBuilder buff = new StringBuilder();
    if (this.domain != null) {
      buff.append(this.domain.getCode());
    }
    if (this.action != null) {
      if (buff.isEmpty()) {
        buff.append("*");
      }
      buff.append(":");
      buff.append(this.action.getCode());
    }
    if (this.instances != null && !this.instances.isEmpty()) {
      if (buff.isEmpty()) {
        buff.append("*:*");
      }
      buff.append(":");
      for (int i = 0; i < this.instances.size(); i++) {
        if (i > 0) {
          buff.append(",");
        }
        buff.append(this.instances.get(i));
      }
    }
    if (buff.isEmpty()) {
      buff.append("*");
    }
    return buff.toString();
  }

  @Override
  public int compareTo(Permission permission) {
    String my = this.getWildcardString();
    String other = permission.getWildcardString();
    return my.compareTo(other);
  }

  @Override
  public String toString() {
    return super.toString() + ",ws" + ":" + getWildcardString();
  }
}
