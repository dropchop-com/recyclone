package com.dropchop.recyclone.model.dto.tag;

import com.dropchop.recyclone.model.dto.DtoId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class Tag extends DtoId implements com.dropchop.recyclone.model.api.tag.Tag {

  private final String type = this.getClass().getSimpleName();

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
  }
}
