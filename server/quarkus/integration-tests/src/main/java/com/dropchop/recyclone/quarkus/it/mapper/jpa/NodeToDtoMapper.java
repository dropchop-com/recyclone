package com.dropchop.recyclone.quarkus.it.mapper.jpa;

import com.dropchop.recyclone.quarkus.it.model.dto.Node;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaNode;
import com.dropchop.recyclone.mapper.api.ToDtoManipulator;
import com.dropchop.recyclone.mapper.api.ToDtoMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = ToDtoManipulator.class
)
public interface NodeToDtoMapper extends ToDtoMapper<Node, JpaNode> {}
