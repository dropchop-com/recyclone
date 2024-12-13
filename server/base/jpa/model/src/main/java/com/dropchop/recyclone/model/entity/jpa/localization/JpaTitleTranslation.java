package com.dropchop.recyclone.model.entity.jpa.localization;

import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.model.entity.jpa.marker.HasJpaLanguage;
import lombok.*;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Embeddable
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class JpaTitleTranslation
  implements HasJpaLanguage, TitleTranslation, HasCreated, HasModified {

  @NonNull
  @Column(name = "lang")
  @EqualsAndHashCode.Include
  private String lang;

  @OneToOne(targetEntity = JpaLanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", insertable = false, updatable = false)
  private JpaLanguage language;

  @NonNull
  @Column(name = "title", length = 1024)
  @EqualsAndHashCode.Exclude
  private String title;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Transient
  transient Boolean base;
}
