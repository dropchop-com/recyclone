package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;

@SuppressWarnings("unused")
public interface RoleNodePermission<
        A extends Action<TDT>,
        D extends Domain<TDT, A>,
        P extends Permission<TDT, A, D>,
        RN extends RoleNode<A, D, P, RN, RNP, TDT>,
        RNP extends RoleNodePermission<A, D, P, RN, RNP, TDT>,
        TDT extends TitleDescriptionTranslation
        > extends Model {

    RN getRoleNode();
    void setRoleNode(RN parent);

    P getPermission();
    void setPermission(P permission);

    Boolean getAllowed();
    void setAllowed(Boolean allowed);

}