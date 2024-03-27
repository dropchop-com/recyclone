package com.dropchop.recyclone.model.dto.invoke;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 06. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RoleParams extends CodeParams {
  private List<UUID> permissionUuids;
}
