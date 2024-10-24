package com.dropchop.recyclone.model.entity.es.localization;

import com.dropchop.recyclone.model.api.localization.Language;
import com.dropchop.recyclone.model.api.marker.HasTags;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.entity.es.base.EsCode;
import com.dropchop.recyclone.model.entity.es.base.EsTitleTranslationHelper;
import com.dropchop.recyclone.model.entity.es.marker.HasEsLanguage;
import com.dropchop.recyclone.model.entity.es.tagging.EsTag;
import lombok.*;

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
public class EsLanguage extends EsCode
  implements Language<EsTitleTranslation>, EsTitleTranslationHelper,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasEsLanguage,
  HasTags<EsTag, EsTitleDescriptionTranslation> {

  static void componentsFromLocale(EsLanguage language, Locale locale) {
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
  private String langCode;

  @Setter(AccessLevel.NONE)
  private Script script;

  @Setter(AccessLevel.NONE)
  private String region;

  @Setter(AccessLevel.NONE)
  private String variant;

  private String title;

  private String lang;

  private EsLanguage language;

  private Set<EsTitleTranslation> translations;

  private List<EsTag> tags;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;


  public EsLanguage(@NonNull String code) {
    this.setCode(code);
    componentsFromLocale(this, toLocale());
  }

  public EsLanguage(@NonNull String iso639, Script script, String region, String variant) {
    this.langCode = iso639;
    this.script = script;
    this.region = region != null ? region.toUpperCase() : null;
    this.variant = variant;
    this.setCode(tagFromComponents());
  }

  public EsLanguage(@NonNull String iso639, Script script, @NonNull EsCountry region, String variant) {
    this(iso639, script, region.getCode(), variant);
  }

  public EsLanguage(@NonNull String iso639, Script script, String region) {
    this(iso639, script, region, null);
  }

  public EsLanguage(@NonNull String iso639, Script script) {
    this(iso639, script, (String)null, null);
  }

  public EsLanguage(@NonNull String iso639, Script script, @NonNull EsCountry region) {
    this(iso639, script, region.getCode(), null);
  }

  public EsLanguage(@NonNull Locale locale) {
    this.fromLocale(locale);
  }

  @Override
  public void setCode(@NonNull String code) {
    super.setCode(code);
    componentsFromLocale(this, toLocale());
  }
}
