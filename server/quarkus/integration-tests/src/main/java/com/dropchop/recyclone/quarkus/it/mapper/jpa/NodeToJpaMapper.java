package com.dropchop.recyclone.quarkus.it.mapper.jpa;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.model.entity.jpa.attr.JpaAttribute;
import com.dropchop.recyclone.quarkus.it.model.dto.Node;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaNode;
import com.dropchop.recyclone.mapper.api.ToEntityMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  builder = @Builder(disableBuilder = true)
)
@SuppressWarnings("unused")
public interface NodeToJpaMapper extends ToEntityMapper<Node, JpaNode> {

  default Set<JpaAttribute<?>> toEntityAttributes(Set<Attribute<?>> value) {
    return new HashSet<>();
  }
}
