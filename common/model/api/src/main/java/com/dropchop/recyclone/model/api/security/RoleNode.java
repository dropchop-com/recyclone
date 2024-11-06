package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;

import java.util.List;

@SuppressWarnings("unused")
public interface RoleNode<
        A extends Action<TDT>,
        D extends Domain<TDT, A>,
        P extends Permission<TDT, A, D>,
        RN extends RoleNode<A, D, P, RN, RNP, TDT>,
        RNP extends RoleNodePermission<A, D, P , RN, RNP, TDT>,
        TDT extends TitleDescriptionTranslation
        > extends Model {

    RN getParent();
    void setParent(RN parent);

    List<RNP> getRoleNodePermissions();
    void setRoleNodePermissions(List<RNP> roleNodePermissions);

    String getEntity();
    void setEntity(String entity);

    String getEntityUuid();
    void setEntityUuid(String entityUuid);

}
