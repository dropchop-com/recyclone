package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.dto.model.security.Domain;
import com.dropchop.recyclone.base.jpa.model.security.JpaDomain;
import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 2. 02. 22.
 */
@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = @Builder(disableBuilder = true),
    uses = {EntityFactoryInvoker.class}
)
public interface DomainToJpaMapper extends ToEntityMapper<Domain, JpaDomain> {

}
