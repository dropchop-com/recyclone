package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.common.Person;
import com.dropchop.recyclone.base.api.model.localization.Country;
import com.dropchop.recyclone.base.api.model.localization.Language;
import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;
import com.dropchop.recyclone.base.api.model.marker.HasTags;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.tagging.Tag;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
@SuppressWarnings("unused")
public interface User<
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
    >
    extends Person<C, L, TT>, PermissionBearer, HasTags<T, TDT>, HasAttributes, HasCreated, HasModified, HasDeactivated {

  Set<R> getRoles();

  void setRoles(Set<R> roles);

  Set<P> getPermissions();

  void setPermissions(Set<P> permissions);

  Set<UA> getAccounts();

  void setAccounts(Set<UA> accounts);

}
