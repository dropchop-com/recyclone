package com.dropchop.recyclone.model.entity.jpa.localization;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import lombok.*;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@MappedSuperclass
@Embeddable
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ETitleTranslation
  implements HasELanguage, TitleTranslation, HasCreated, HasModified {

  @NonNull
  @Column(name = "lang")
  @EqualsAndHashCode.Include
  private String lang;

  @OneToOne(targetEntity = ELanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", insertable = false, updatable = false)
  private ELanguage language;

  @NonNull
  @Column(name = "title", length = 1024)
  @EqualsAndHashCode.Exclude
  private String title;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  transient Boolean base;
}
