package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext.Listener;
import com.dropchop.recyclone.base.api.model.invoke.SecurityExecContext;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;
import com.dropchop.recyclone.base.api.model.security.annotations.Logical;
import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * Container for common current execution variables.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 15. 03. 22.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class DefaultExecContext<D extends Dto>
  extends ParamsExecContext<Listener>
  implements CommonExecContext<D, Listener>, SecurityExecContext, HasAttributes {

  @NonNull
  private List<D> data;

  private List<String> requiredPermissions;

  private Logical requiredPermissionsOp = Logical.AND;

  private List<String> requiredRoles;

  private Logical requiredRolesOp = Logical.AND;

  private Boolean requiredGuest;

  private Boolean requiredAuthenticated;

  private Set<Attribute<?>> attributes;
}
