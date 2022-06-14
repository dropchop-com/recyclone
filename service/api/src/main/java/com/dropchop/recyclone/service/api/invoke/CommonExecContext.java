package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.DataExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContext.Listener;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.security.annotations.Logical;
import com.dropchop.recyclone.model.dto.invoke.ParamsExecContext;
import lombok.*;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * Container for common current execution variables.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 15. 03. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class CommonExecContext<P extends Params, D extends Dto>
  extends ParamsExecContext<P, Listener>
  implements DataExecContext<D, Listener>, SecurityExecContext {

  @NonNull
  Subject subject;

  @NonNull
  List<D> data;

  /**
   * Default shiro security domain used in current execution
   * (First domain used in @see org.apache.shiro.authz.annotation.RequiresPermissions) REST layer.
   */
  String securityDomain;

  /**
   * Default shiro security action used in current execution
   * (First action used in @see org.apache.shiro.authz.annotation.RequiresPermissions) REST layer.
   */
  String securityAction;


  List<String> requiredPermissions;
  Logical requiredPermissionsOp;
}
