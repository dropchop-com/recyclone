package com.dropchop.recyclone.base.api.model.common;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.Country;
import com.dropchop.recyclone.base.api.model.localization.Language;
import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
@SuppressWarnings("unused")
public interface Person<C extends Country<TT>, L extends Language<TT>, TT extends TitleTranslation>
    extends Model, HasUuid {

  C getCountry();

  void setCountry(C country);

  L getLanguage();

  void setLanguage(L language);

  String getFirstName();
  void setFirstName(String firstName);

  String getLastName();
  void setLastName(String lastName);

  String getMiddleName();

  void setMiddleName(String middleName);

  String getMaidenName();

  void setMaidenName(String maidenName);

  String getTitle();

  void setTitle(String title);

  String getInitials();

  void setInitials(String initials);

  Boolean getFemale();

  void setFemale(Boolean female);

  String getDefaultEmail();

  void setDefaultEmail(String defaultEmail);

  String getDefaultPhone();

  void setDefaultPhone(String defaultPhone);

}
