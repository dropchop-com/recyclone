package com.dropchop.recyclone.model.entity.jpa.localization;

import com.dropchop.recyclone.model.api.localization.Language;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.entity.jpa.base.ECode;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 01. 22.
 */
@Getter
@Setter
@Entity
@Table(name = "language")
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@SuppressWarnings("JpaDataSourceORMInspection")
public class ELanguage extends ECode
  implements HasCreated, HasModified, HasDeactivated, HasELanguage, Language<ETitleTranslation> {

  static void componentsFromLocale(ELanguage language, Locale locale) {
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

  @OneToOne(targetEntity = ELanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "language_fk_language_code"))
  private ELanguage language;

  @ElementCollection
  @CollectionTable(
    name="language_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_language_l_fk_language_code_lang", columnNames = {"fk_language_code", "lang"}),
    foreignKey = @ForeignKey(name = "language_l_fk_language_code"),
    joinColumns=@JoinColumn(name="fk_language_code")
  )
  private Set<ETitleTranslation> translations;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;


  public ELanguage(@NonNull String code) {
    this.setCode(code);
    componentsFromLocale(this, toLocale());
  }

  public ELanguage(@NonNull String iso639, Script script, String region, String variant) {
    this.langCode = iso639;
    this.script = script;
    this.region = region != null ? region.toUpperCase() : null;
    this.variant = variant;
    this.setCode(tagFromComponents());
  }

  public ELanguage(@NonNull String iso639, Script script, @NonNull ECountry region, String variant) {
    this(iso639, script, region.getCode(), variant);
  }

  public ELanguage(@NonNull String iso639, Script script, String region) {
    this(iso639, script, region, null);
  }

  public ELanguage(@NonNull String iso639, Script script) {
    this(iso639, script, (String)null, null);
  }

  public ELanguage(@NonNull String iso639, Script script, @NonNull ECountry region) {
    this(iso639, script, region.getCode(), null);
  }

  public ELanguage(@NonNull Locale locale) {
    this.fromLocale(locale);
  }

  @Override
  public void setCode(@NonNull String code) {
    super.setCode(code);
    componentsFromLocale(this, toLocale());
  }
}
