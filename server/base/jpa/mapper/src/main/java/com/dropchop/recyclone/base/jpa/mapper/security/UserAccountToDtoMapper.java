package com.dropchop.recyclone.base.jpa.mapper.security;

import com.dropchop.recyclone.base.api.mapper.DtoPolymorphicFactory;
import com.dropchop.recyclone.base.dto.model.security.LoginAccount;
import com.dropchop.recyclone.base.dto.model.security.TokenAccount;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaLoginAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaTokenAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaUserAccount;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.ToDtoManipulator;
import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import org.mapstruct.*;

@Mapper(
    componentModel = "jakarta-cdi",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true),
    uses = {
        ToDtoManipulator.class, DtoPolymorphicFactory.class, UserToDtoMapper.class
    }
)
public interface UserAccountToDtoMapper extends ToDtoMapper<UserAccount, JpaUserAccount> {

  @SubclassMapping( source = JpaLoginAccount.class, target = LoginAccount.class)
  @SubclassMapping( source = JpaTokenAccount.class, target = TokenAccount.class)
  UserAccount toDto(JpaUserAccount entity, @Context MappingContext context);
}
