package com.dropchop.recyclone.base.jpa.mapper.security;


import com.dropchop.recyclone.base.api.mapper.EntityFactoryInvoker;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaUserAccount;
import org.mapstruct.*;

@Mapper(
  componentModel = "jakarta-cdi",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
  uses = EntityFactoryInvoker.class,
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  builder = @Builder(disableBuilder = true)
)
public interface UserAccountToJpaMapper  extends ToEntityMapper<UserAccount, JpaUserAccount> {
 /* @SubclassMapping( source = LoginAccount.class, target =  JpaLoginAccount.class)
  @SubclassMapping( source = TokenAccount.class, target = JpaTokenAccount.class)
  JpaUserAccount toEntity(UserAccount dto, @Context MappingContext context);*/
}
