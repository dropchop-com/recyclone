package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.localization.Country;
import com.dropchop.recyclone.base.api.model.localization.Language;
import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.api.model.tagging.Tag;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
@SuppressWarnings("unused")
public interface TokenAccount<
  U extends User<UA, TDT, TT, A, D, P, R, C, L, T>,
  UA extends UserAccount,
  TDT extends TitleDescriptionTranslation,
  TT extends TitleTranslation,
  A extends Action<TDT>,
  D extends Domain<TDT, A>,
  P extends Permission<TDT, A, D>,
  R extends Role<TDT, A, D, P>,
  C extends Country<TT>,
  L extends Language<TT>,
  T extends Tag<T, TDT>
  > extends UserAccount<U, UA, TDT, TT, A, D, P, R, C, L, T> {
  String getToken();
  void setToken(String token);
}
