package com.dropchop.recyclone.test.service.jpa.blaze;

import com.dropchop.recyclone.service.api.mapping.ToDtoManipulator;
import com.dropchop.recyclone.service.api.mapping.ToDtoMapper;
import com.dropchop.recyclone.test.model.dto.Dummy;
import com.dropchop.recyclone.test.model.entity.jpa.EDummy;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = ToDtoManipulator.class
)
public interface DummyToDtoMapper extends ToDtoMapper<Dummy, EDummy> {
}
