package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.security.LoginAccount;
import com.dropchop.recyclone.model.dto.security.TokenAccount;
import com.dropchop.recyclone.model.dto.security.UserAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ELoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ETokenAccount;
import com.dropchop.recyclone.model.entity.jpa.security.EUserAccount;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.FilterDtoMapping;
import org.mapstruct.*;

@Mapper(
    componentModel = "jakarta-cdi",
    uses = {FilterDtoMapping.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface UserAccountToDtoMapper {
  //TODO: try to generalize this
  @SubclassMapping( source = ELoginAccount.class, target = LoginAccount.class)
  @SubclassMapping( source = ETokenAccount.class, target = TokenAccount.class)
  UserAccount toDto(EUserAccount tags, @Context MappingContext context);
}
