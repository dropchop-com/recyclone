package com.dropchop.recyclone.service.jpa.blaze.test;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.test.Node;
import com.dropchop.recyclone.model.entity.jpa.test.ENode;
import com.dropchop.recyclone.service.api.mapping.ToDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@Mapper(componentModel = "cdi", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NodeToDtoMapper extends ToDtoMapper<Node, CodeParams, ENode> {}
