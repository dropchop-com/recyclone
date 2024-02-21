package com.dropchop.recyclone.service.jpa.blaze.test;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.dto.test.Node;
import com.dropchop.recyclone.model.entity.jpa.attr.EAttribute;
import com.dropchop.recyclone.model.entity.jpa.test.ENode;
import com.dropchop.recyclone.service.api.mapping.ToEntityMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  builder = @Builder(disableBuilder = true)
)
@SuppressWarnings("unused")
public interface NodeToEntityMapper extends ToEntityMapper<Node, ENode> {

  default Set<EAttribute<?>> toEntityAttributes(Set<Attribute<?>> value) {
    return new HashSet<>();
  }
}
