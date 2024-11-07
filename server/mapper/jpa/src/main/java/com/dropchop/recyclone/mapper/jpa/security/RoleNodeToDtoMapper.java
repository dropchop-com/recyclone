package com.dropchop.recyclone.mapper.jpa.security;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.api.ToDtoManipulator;
import com.dropchop.recyclone.mapper.api.ToDtoMapper;
import com.dropchop.recyclone.model.dto.security.RoleInstanceNode;
import com.dropchop.recyclone.model.dto.security.RoleNode;
import com.dropchop.recyclone.model.dto.security.RoleTemplateNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleInstanceNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleNode;
import com.dropchop.recyclone.model.entity.jpa.security.JpaRoleTemplateNode;
import org.mapstruct.*;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = ToDtoManipulator.class
)
public interface RoleNodeToDtoMapper extends ToDtoMapper<RoleNode, JpaRoleNode> {
  @SubclassMapping( source = JpaRoleInstanceNode.class, target = RoleInstanceNode.class)
  @SubclassMapping( source = JpaRoleTemplateNode.class, target = RoleTemplateNode.class)
  RoleNode toDto(JpaRoleNode node, @Context MappingContext context);
}
