package com.dropchop.recyclone.mapper.jpa.security;

import com.dropchop.recyclone.base.api.mapper.DtoPolymorphicFactory;
import com.dropchop.recyclone.base.dto.model.security.LoginAccount;
import com.dropchop.recyclone.base.dto.model.security.TokenAccount;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.recyclone.model.entity.jpa.security.JpaLoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.JpaTokenAccount;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUserAccount;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import org.mapstruct.*;

@Mapper(
    componentModel = "jakarta-cdi",
    uses = {ToDtoManipulator.class, DtoPolymorphicFactory.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface UserAccountToDtoMapper extends ToDtoMapper<UserAccount, JpaUserAccount> {
  @SubclassMapping( source = JpaLoginAccount.class, target = LoginAccount.class)
  @SubclassMapping( source = JpaTokenAccount.class, target = TokenAccount.class)
  UserAccount toDto(JpaUserAccount tags, @Context MappingContext context);
}
