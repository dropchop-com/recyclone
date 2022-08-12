package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.CommonExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContext.Listener;
import com.dropchop.recyclone.model.api.invoke.SecurityExecContext;
import com.dropchop.recyclone.model.api.security.annotations.Logical;
import lombok.*;

import java.util.List;

/**
 * Container for common current execution variables.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 15. 03. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class DefaultExecContext<D extends Dto>
  extends ParamsExecContext<Listener>
  implements CommonExecContext<D, Listener>, SecurityExecContext {

  @NonNull
  List<D> data;

  List<String> requiredPermissions;

  Logical requiredPermissionsOp = Logical.AND;
}
