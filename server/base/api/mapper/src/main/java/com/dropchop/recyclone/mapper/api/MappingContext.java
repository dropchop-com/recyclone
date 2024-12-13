package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.DataExecContext;
import com.dropchop.recyclone.base.api.model.invoke.SecurityExecContext;
import com.dropchop.recyclone.base.api.model.security.annotations.Logical;
import com.dropchop.recyclone.model.dto.invoke.ParamsExecContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 04. 22.
 */
@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class MappingContext
  extends ParamsExecContext<MappingListener>
  implements DataExecContext<Dto, MappingListener>, SecurityExecContext, TotalCountExecContextListener {

  private long totalCount;

  private List<Dto> data;

  List<String> requiredPermissions;

  Logical requiredPermissionsOp = Logical.AND;

  List<String> requiredRoles;

  Logical requiredRolesOp = Logical.AND;

  Boolean requiredGuest;

  Boolean requiredAuthenticated;

  public MappingContext of(CommonExecContext<?, ?> sourceContext) {
    super.of(sourceContext);
    //noinspection unchecked
    this.setData((List<Dto>) sourceContext.getData());
    this.setRequiredPermissions(sourceContext.getRequiredPermissions());
    this.setRequiredPermissionsOp(sourceContext.getRequiredPermissionsOp());
    return this;
  }

  public MappingContext() {
    //this is to ensure mapping works for polymorphic tags.
    this.afterMapping(new SetDtoName());
    this.afterMapping(new SetEntityName());
  }

  public void onTotalCount(Long count) {
    if (count == null) {
      return;
    }
    this.setTotalCount(count);
  }

  public long totalCount() {
    return this.getTotalCount();
  }

  public MappingContext totalCount(long totalCount) {
    this.setTotalCount(totalCount);
    return this;
  }

  @Override
  public MappingContext listener(MappingListener listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }

  public MappingContext createWith(FactoryMappingListener listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }

  public MappingContext beforeMapping(BeforeMappingListener listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }

  public MappingContext afterMapping(AfterMappingListener listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }
}
