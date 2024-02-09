package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.security.EDomain;
import com.dropchop.recyclone.model.entity.jpa.security.EUser;
import com.dropchop.recyclone.service.api.mapping.ToDtoMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 2. 02. 22.
 */
@Mapper(
  componentModel = "cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  builder = @Builder(disableBuilder = true)
)
public interface UserToDtoMapper extends ToDtoMapper<User<DtoId>, EUser<EUuid>> {

}
