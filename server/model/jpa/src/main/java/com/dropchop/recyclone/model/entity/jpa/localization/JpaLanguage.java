package com.dropchop.recyclone.model.entity.jpa.localization;

import com.dropchop.recyclone.model.api.localization.Language;
import com.dropchop.recyclone.model.api.marker.HasTags;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.entity.jpa.base.JpaCode;
import com.dropchop.recyclone.model.entity.jpa.base.JpaTitleTranslationHelper;
import com.dropchop.recyclone.model.entity.jpa.marker.HasJpaLanguage;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import lombok.*;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "language")
public class JpaLanguage extends JpaCode
  implements Language<JpaTitleTranslation>, JpaTitleTranslationHelper,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasJpaLanguage,
  HasTags<JpaTag, JpaTitleDescriptionTranslation> {

  static void componentsFromLocale(JpaLanguage language, Locale locale) {
    language.langCode = locale.getLanguage();
    String tmp = locale.getScript();
    if (tmp != null && !tmp.isEmpty()) {
      try {
        language.script = Script.valueOf(tmp);
      } catch (IllegalArgumentException e) {
        //ignore
      }
    }
    tmp = locale.getCountry();
    if (tmp != null && !tmp.isEmpty()) {
      language.region = tmp;
    }
    tmp = locale.getVariant();
    if (tmp != null && !tmp.isEmpty()) {
      language.variant = tmp;
    }
  }

  @Setter(AccessLevel.NONE)
  @Transient
  private String langCode;

  @Setter(AccessLevel.NONE)
  @Transient
  private Script script;

  @Setter(AccessLevel.NONE)
  @Transient
  private String region;

  @Setter(AccessLevel.NONE)
  @Transient
  private String variant;

  @Column(name="title")
  private String title;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(targetEntity = JpaLanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "language_fk_language_code"))
  private JpaLanguage language;

  @ElementCollection
  @CollectionTable(
    name="language_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_language_l_fk_language_code_lang", columnNames = {"fk_language_code", "lang"}),
    foreignKey = @ForeignKey(name = "language_l_fk_language_code"),
    joinColumns = @JoinColumn(name="fk_language_code")
  )
  private Set<JpaTitleTranslation> translations;

  @ManyToMany
  @JoinTable(
    name="language_t",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_language_t_fk_language_code_fk_tag_uuid", columnNames = {"fk_language_code", "fk_tag_uuid"}),
    joinColumns = @JoinColumn( name="fk_language_code", foreignKey = @ForeignKey(name = "language_t_fk_language_code")),
    inverseJoinColumns = @JoinColumn( name="fk_tag_uuid", foreignKey = @ForeignKey(name = "language_t_fk_tag_uuid"))
  )
  @OrderColumn(name = "idx")
  private List<JpaTag> tags;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;


  public JpaLanguage(@NonNull String code) {
    this.setCode(code);
    componentsFromLocale(this, toLocale());
  }

  public JpaLanguage(@NonNull String iso639, Script script, String region, String variant) {
    this.langCode = iso639;
    this.script = script;
    this.region = region != null ? region.toUpperCase() : null;
    this.variant = variant;
    this.setCode(tagFromComponents());
  }

  public JpaLanguage(@NonNull String iso639, Script script, @NonNull JpaCountry region, String variant) {
    this(iso639, script, region.getCode(), variant);
  }

  public JpaLanguage(@NonNull String iso639, Script script, String region) {
    this(iso639, script, region, null);
  }

  public JpaLanguage(@NonNull String iso639, Script script) {
    this(iso639, script, (String)null, null);
  }

  public JpaLanguage(@NonNull String iso639, Script script, @NonNull JpaCountry region) {
    this(iso639, script, region.getCode(), null);
  }

  public JpaLanguage(@NonNull Locale locale) {
    this.fromLocale(locale);
  }

  @Override
  public void setCode(@NonNull String code) {
    super.setCode(code);
    componentsFromLocale(this, toLocale());
  }
}
