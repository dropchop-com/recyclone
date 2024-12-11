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

    /**
     * Role node target defines target group for role nodes.
     * Explanation: Role nodes can define permission set for target groups but do not refer to specific system entity.
     */
    String getTarget();
    void setTarget(String target);

    String getTargetId();
    void setTargetId(String targetId);

    /**
     * Defines parent role node
     */
    RN getParent();
    void setParent(RN parent);

    /**
     * Defines permissions set on role node
     */
    List<RNP> getRoleNodePermissions();
    void setRoleNodePermissions(List<RNP> roleNodePermissions);

    /**
     * Defines specific system entity role node refers to.
     */
    String getEntity();
    void setEntity(String entity);

    String getEntityId();
    void setEntityId(String entityId);

    //Defines how many levels up on hierarchy
    Integer getMaxParentInstanceLevel();
    void setMaxParentInstanceLevel(Integer maxParentInstanceLevel);



}
