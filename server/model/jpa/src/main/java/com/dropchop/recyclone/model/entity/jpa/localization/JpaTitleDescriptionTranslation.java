package com.dropchop.recyclone.model.entity.jpa.localization;

import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Embeddable
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JpaTitleDescriptionTranslation
  implements TitleDescriptionTranslation {

  @NonNull
  @Column(name = "lang")
  @EqualsAndHashCode.Include
  private String lang;

  @OneToOne(targetEntity = JpaLanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", insertable = false, updatable = false)
  private JpaLanguage language;

  @NonNull
  @Column(name = "title", length = 1024)
  private String title;

  @Column(name = "description", length = 8096)
  private String description;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Transient
  transient Boolean base;
}
