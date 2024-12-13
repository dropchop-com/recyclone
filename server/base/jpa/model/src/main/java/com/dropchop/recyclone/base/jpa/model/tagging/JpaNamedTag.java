package com.dropchop.recyclone.base.jpa.model.tagging;

import com.dropchop.recyclone.base.api.model.tagging.NamedTag;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 06. 22.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Entity
@SuppressWarnings("JpaDataSourceORMInspection")
public class JpaNamedTag extends JpaTag
  implements NamedTag<JpaTag, JpaTitleDescriptionTranslation> {

  @NonNull
  @Column(name="name")
  private String name;

  @Override
  public String toString() {
    return super.toString() + ",n" + ":" + getName();
  }
}
