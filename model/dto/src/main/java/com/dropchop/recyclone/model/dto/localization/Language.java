package com.dropchop.recyclone.model.dto.localization;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Language representing BCP 47 language tag (ISO 639-3 code - script - region)
 * with name based uuid constructed from "Language.[BCP 74 tag]" string.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class Language extends DtoCode
  implements HasCreated, HasModified, HasDeactivated, com.dropchop.recyclone.model.api.localization.Language<TitleTranslation> {

  static void componentsFromLocale(Language language, Locale locale) {
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

  @NonNull
  private String code;

  @JsonIgnore
  @Setter(AccessLevel.NONE)
  private String langCode;

  @JsonIgnore
  @Setter(AccessLevel.NONE)
  private Script script;

  @JsonIgnore
  @Setter(AccessLevel.NONE)
  private String region;

  @JsonIgnore
  @Setter(AccessLevel.NONE)
  private String variant;

  @JsonInclude(NON_NULL)
  private String title;

  @JsonInclude(NON_NULL)
  private String lang;

  @JsonInclude(NON_EMPTY)
  private Set<TitleTranslation> translations;

  @JsonInclude(NON_NULL)
  private ZonedDateTime created;

  @JsonInclude(NON_NULL)
  private ZonedDateTime modified;

  @JsonInclude(NON_NULL)
  private ZonedDateTime deactivated;


  public Language(@NonNull String code) {
    this.code = code;
    componentsFromLocale(this, toLocale());
  }

  public Language(@NonNull String iso639, Script script, String region, String variant) {
    this.langCode = iso639;
    this.script = script;
    this.region = region != null ? region.toUpperCase() : null;
    this.variant = variant;
    this.code = tagFromComponents();
  }

  public Language(@NonNull String iso639, Script script, @NonNull Country region, String variant) {
    this(iso639, script, region.getCode(), variant);
  }

  public Language(@NonNull String iso639, Script script, String region) {
    this(iso639, script, region, null);
  }

  public Language(@NonNull String iso639, Script script) {
    this(iso639, script, (String)null, null);
  }

  public Language(@NonNull String iso639, Script script, @NonNull Country region) {
    this(iso639, script, region.getCode(), null);
  }

  public Language(@NonNull Locale locale) {
    this.fromLocale(locale);
  }

  @Override
  public void setCode(@NonNull String code) {
    this.code = code;
    componentsFromLocale(this, toLocale());
  }
}
