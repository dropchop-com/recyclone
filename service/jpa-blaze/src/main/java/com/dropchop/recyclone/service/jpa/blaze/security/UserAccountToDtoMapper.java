package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.security.LoginAccount;
import com.dropchop.recyclone.model.dto.security.TokenAccount;
import com.dropchop.recyclone.model.dto.security.UserAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ELoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ETokenAccount;
import com.dropchop.recyclone.model.entity.jpa.security.EUserAccount;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.ToDtoManipulator;
import com.dropchop.recyclone.service.api.mapping.ToDtoMapper;
import org.mapstruct.*;

@Mapper(
    componentModel = "jakarta-cdi",
    uses = {ToDtoManipulator.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface UserAccountToDtoMapper extends ToDtoMapper<UserAccount, EUserAccount> {

  @SubclassMapping( source = ELoginAccount.class, target = LoginAccount.class)
  @SubclassMapping( source = ETokenAccount.class, target = TokenAccount.class)
  UserAccount toDto(EUserAccount tags, @Context MappingContext context);
}
