package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.service.api.invoke.SecurityExecContext;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 06. 22.
 */
public class JoinEntityHelper<E extends Entity, JE extends Entity, JID> {

  public interface Joiner<E extends Entity, JE extends Entity> {
    void join(E entity, Collection<JE> joined);
  }

  public interface Permitter<JID> {
    boolean permit(JID joinedId);
  }

  public interface JoinedIdSupplier<JID, E extends Entity> {
    List<JID> get(E toJoin);
  }

  public class ViewPermitter<ID> implements Permitter<ID> {
    private final SecurityExecContext ctx;

    public ViewPermitter(SecurityExecContext ctx) {
      this.ctx = ctx;
    }

    @Override
    public boolean permit(ID joined) {
      String securityDomain = JoinEntityHelper.this.service.getSecurityDomain();
      Subject subject = ctx.getSubject();
      return subject.isPermitted(
        Constants.Permission.compose(securityDomain, Constants.Actions.VIEW, joined.toString())
      );
    }
  }

  private final EntityByIdService<?, JE, JID> service;
  private final Collection<E> parentEntities;

  public JoinEntityHelper(EntityByIdService<?, JE, JID> service) {
    this.service = service;
    this.parentEntities = null;
  }

  public JoinEntityHelper(EntityByIdService<?, JE, JID> service, Collection<E> parentEntities) {
    this.service = service;
    this.parentEntities = parentEntities;
  }

  public Collection<E> join(Collection<E> parentEntities, JoinedIdSupplier<JID, E> joinedIdSupplier,
                            Permitter<JID> joinPermitter, Joiner<E, JE> joinApplier) {
    if (parentEntities == null) {
      return null;
    }
    for (E entity : parentEntities) {

      Collection<JID> ids = joinedIdSupplier.get(entity);
      if (!ids.iterator().hasNext()) {
        throw new ServiceException(ErrorCode.parameter_validation_error, "Missing ids for joined model!");
      }

      Collection<JID> permitted = joinPermitter != null ? new ArrayList<>() : ids;
      if (joinPermitter != null) {
        for (JID joinId : ids) {
          if (joinPermitter.permit(joinId)) {
            permitted.add(joinId);
          }
        }
      }
      Collection<JE> joined = service.findById(permitted);
      if (joined.size() != permitted.size()) {
        throw new ServiceException(ErrorCode.data_validation_error,
          "Loaded joined entities size does not match join model permitted ids size!");
      }

      joinApplier.join(entity, joined);
    }
    return parentEntities;
  }

  public Collection<E> join(Collection<E> parentEntities,
                            JoinedIdSupplier<JID, E> joinedIdSupplier,
                            Joiner<E, JE> joinApplier) {
    return this.join(parentEntities, joinedIdSupplier, null, joinApplier);
  }

  public Collection<E> join(JoinedIdSupplier<JID, E> joinedIdSupplier,
                            Permitter<JID> joinPermitter,
                            Joiner<E, JE> joinApplier) {
    return join(this.parentEntities, joinedIdSupplier, joinPermitter, joinApplier);
  }

  public Collection<E> join(JoinedIdSupplier<JID, E> joinedIdSupplier,
                            Joiner<E, JE> joinApplier) {
    return join(this.parentEntities, joinedIdSupplier, null, joinApplier);
  }
}
