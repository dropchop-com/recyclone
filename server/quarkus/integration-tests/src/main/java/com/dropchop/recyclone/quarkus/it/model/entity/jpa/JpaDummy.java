package com.dropchop.recyclone.quarkus.it.model.entity.jpa;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.entity.jpa.base.JpaCode;
import com.dropchop.recyclone.model.entity.jpa.base.JpaTitleDescriptionTranslationHelper;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasJpaLanguage;
import com.dropchop.recyclone.quarkus.it.model.api.Dummy;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 03. 24.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dummy")
@SuppressWarnings("unused")
public class JpaDummy extends JpaCode
    implements Dummy<JpaTitleDescriptionTranslation>, JpaTitleDescriptionTranslationHelper,
    HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasJpaLanguage {

  @Column(name="title")
  private String title;

  @Column(name="description")
  private String description;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(targetEntity = JpaLanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "dummy_fk_language_code"))
  private JpaLanguage language;

  @ElementCollection
  @CollectionTable(
      name="dummy_l",
      uniqueConstraints = @UniqueConstraint(
          name = "uq_dummy_l_fk_language_code_lang", columnNames = {"fk_dummy_code", "lang"}
      ),
      foreignKey = @ForeignKey(name = "dummy_l_fk_country_code"),
      joinColumns = @JoinColumn(name="fk_dummy_code")
  )
  private Set<JpaTitleDescriptionTranslation> translations;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;

  public JpaDummy(@NonNull String code) {
    super(code);
  }
}
