package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;

@SuppressWarnings("unused")
public interface RoleNodePermissionTemplate<
        A extends Action<TDT>,
        D extends Domain<TDT, A>,
        P extends Permission<TDT, A, D>,
        RN extends RoleNode<A, D, P, RN, RNP, TDT>,
        RNP extends RoleNodePermission<A, D, P, RN, RNP, TDT>,
        TDT extends TitleDescriptionTranslation
        > extends RoleNodePermission<A, D, P, RN, RNP, TDT> {

    String getTarget();
    void setTarget(String template);

    String getTargetId();
    void setTargetId(String targetId);

}
