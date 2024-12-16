package com.dropchop.recyclone.base.dto.model.invoke;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext.Listener;
import com.dropchop.recyclone.base.api.model.invoke.SecurityExecContext;
import com.dropchop.recyclone.base.api.model.security.annotations.Logical;
import lombok.*;

import java.util.List;

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
  implements CommonExecContext<D, Listener>, SecurityExecContext {

  @NonNull
  List<D> data;

  List<String> requiredPermissions;

  Logical requiredPermissionsOp = Logical.AND;

  List<String> requiredRoles;

  Logical requiredRolesOp = Logical.AND;

  Boolean requiredGuest;

  Boolean requiredAuthenticated;
}
