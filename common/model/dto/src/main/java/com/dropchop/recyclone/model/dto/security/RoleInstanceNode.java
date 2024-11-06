package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class RoleInstanceNode extends RoleNode implements com.dropchop.recyclone.model.api.security.RoleInstanceNode<
        Action, Domain, Permission, RoleNode, RoleNodePermission, TitleDescriptionTranslation> {

}
