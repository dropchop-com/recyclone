package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;

import java.util.List;

@SuppressWarnings("unused")
public interface RoleNode<
        RN extends RoleNode<RN>
        > extends Model, HasCreated, HasModified {

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
     * Defines specific system entity role node refers to.
     */
    String getEntity();
    void setEntity(String entity);

    String getEntityId();
    void setEntityId(String entityId);

    String getEntityName();
    void setEntityName(String entityName);

    //Defines how many levels up on hierarchy
    Integer getMaxParentInstanceLevel();
    void setMaxParentInstanceLevel(Integer maxParentInstanceLevel);



}
