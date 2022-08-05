package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.common.Person;
import com.dropchop.recyclone.model.api.localization.Country;
import com.dropchop.recyclone.model.api.localization.Language;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasTags;
import com.dropchop.recyclone.model.api.tagging.Tag;

import java.util.Set;
import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@SuppressWarnings("unused")
public interface User<
    UA extends UserAccount,
    TT extends TitleTranslation,
    A extends Action<TT>,
    D extends Domain<TT, A>,
    P extends Permission<TT, A, D>,
    R extends Role<TT, A, D, P>,
    O extends Model,
    C extends Country<TT>,
    L extends Language<TT>,
    T extends Tag<T, TT>
    >
    extends Person<C, L, TT>, PermissionBearer, HasTags<T, TT> {

  SortedSet<R> getRoles();

  void setRoles(SortedSet<R> roles);

  SortedSet<P> getPermissions();

  void setPermissions(SortedSet<P> permissions);

  Set<UA> getAccounts();

  void setAccounts(Set<UA> accounts);

  O getOwner();

  void setOwner(O owner);
}
