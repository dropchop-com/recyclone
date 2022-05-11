package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.dto.invoke.ParamsExecContext;
import com.dropchop.recyclone.repo.api.ctx.TotalCountExecContextListener;
import com.dropchop.recyclone.service.api.CommonExecContext;
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
  extends ParamsExecContext<P, MappingListener<P>> implements TotalCountExecContextListener {

  private long totalCount;
  private String securityAction;
  private String securityDomain;
  private Subject subject;

  public MappingContext<P> of(CommonExecContext<?, ?> sourceContext) {
    super.of(sourceContext);
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
    super.listener(listener);
    return this;
  }
}
