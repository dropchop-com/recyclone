package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.DataExecContext;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.dto.invoke.ParamsExecContext;
import com.dropchop.recyclone.repo.api.ctx.TotalCountExecContextListener;
import com.dropchop.recyclone.service.api.mapping.AfterMappingListener;
import com.dropchop.recyclone.service.api.mapping.BeforeMappingListener;
import com.dropchop.recyclone.service.api.mapping.FactoryMappingListener;
import com.dropchop.recyclone.service.api.mapping.MappingListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 04. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class MappingContext<P extends Params>
  extends ParamsExecContext<P, MappingListener<P>>
  implements TotalCountExecContextListener, DataExecContext<Dto, MappingListener<P>>, SecurityExecContext {

  private long totalCount;
  private String securityAction;
  private String securityDomain;
  private Subject subject;
  private List<Dto> data;


  public MappingContext<P> of(CommonExecContext<?, ?> sourceContext) {
    super.of(sourceContext);
    //noinspection unchecked
    this.setData((List<Dto>) sourceContext.getData());
    this.setSubject(sourceContext.getSubject());
    this.setSecurityAction(sourceContext.getSecurityAction());
    this.setSecurityDomain(sourceContext.getSecurityDomain());
    return this;
  }

  @Override
  public void onTotalCount(Long count) {
    if (count == null) {
      return;
    }
    this.setTotalCount(count);
  }

  public long totalCount() {
    return this.getTotalCount();
  }

  public MappingContext<P> totalCount(long totalCount) {
    this.setTotalCount(totalCount);
    return this;
  }

  public String securityAction() {
    return this.getSecurityAction();
  }

  public MappingContext<P> securityAction(String securityAction) {
    this.setSecurityAction(securityAction);
    return this;
  }

  public String securityDomain() {
    return this.getSecurityDomain();
  }

  public MappingContext<P> securityDomain(String securityDomain) {
    this.setSecurityDomain(securityDomain);
    return this;
  }

  @Override
  public MappingContext<P> listeners(List<MappingListener<P>> listeners) {
    super.listeners(listeners);
    return this;
  }

  @Override
  public MappingContext<P> listener(MappingListener<P> listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }

  public MappingContext<P> createWith(FactoryMappingListener<P> listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }

  public MappingContext<P> beforeMapping(BeforeMappingListener<P> listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }

  public MappingContext<P> afterMapping(AfterMappingListener<P> listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }
}