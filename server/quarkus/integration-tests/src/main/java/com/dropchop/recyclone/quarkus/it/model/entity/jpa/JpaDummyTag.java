package com.dropchop.recyclone.quarkus.it.model.entity.jpa;

import com.dropchop.recyclone.base.api.model.marker.HasOptionalTranslation;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;
import com.dropchop.recyclone.base.jpa.model.tagging.JpaNamedTag;
import com.dropchop.recyclone.base.jpa.model.tagging.JpaTag;
import com.dropchop.recyclone.quarkus.it.model.api.DummyTag;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4/18/25.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("DummyTag")
@SuppressWarnings("unused")
public class JpaDummyTag extends JpaNamedTag
    implements DummyTag<JpaTag, JpaTitleDescriptionTranslation>, HasOptionalTranslation {
  public JpaDummyTag(@NonNull String name) {
    super(name);
  }
}
