package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.model.dto.security.LoginAccount;
import com.dropchop.recyclone.model.dto.security.TokenAccount;
import com.dropchop.recyclone.model.dto.security.UserAccount;
import com.dropchop.recyclone.model.entity.jpa.security.JpaLoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.JpaTokenAccount;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUserAccount;
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
public interface UserAccountToDtoMapper extends ToDtoMapper<UserAccount, JpaUserAccount> {

  @SubclassMapping( source = JpaLoginAccount.class, target = LoginAccount.class)
  @SubclassMapping( source = JpaTokenAccount.class, target = TokenAccount.class)
  UserAccount toDto(JpaUserAccount tags, @Context MappingContext context);
}
