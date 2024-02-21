package com.dropchop.recyclone.service.jpa.blaze.security;

import com.dropchop.recyclone.model.dto.security.LoginAccount;
import com.dropchop.recyclone.model.dto.security.TokenAccount;
import com.dropchop.recyclone.model.dto.security.UserAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ELoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ETokenAccount;
import com.dropchop.recyclone.model.entity.jpa.security.EUserAccount;
import org.mapstruct.SubclassMapping;

public interface UserAccountToDtoMapper {

  @SubclassMapping( source = ELoginAccount.class, target = LoginAccount.class)
  @SubclassMapping( source = ETokenAccount.class, target = TokenAccount.class)
  UserAccount toDto(EUserAccount tags);

}
