package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.api.model.invoke.SecurityExecContext;
import com.dropchop.recyclone.repo.api.ReadRepository;
import com.dropchop.recyclone.service.api.security.AuthorizationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 06. 22.
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
      return authorizationService.isPermitted(
        Constants.Permission.compose(securityDomain, Constants.Actions.VIEW, joined.toString())
      );
    }
  }

  private final ReadRepository<JE, JID> repository;
  private final String securityDomain;
  private final AuthorizationService authorizationService;
  private final Collection<E> parentEntities;

  public JoinEntityHelper(AuthorizationService authorizationService,
                          String securityDomain,
                          ReadRepository<JE, JID> repository) {
    this.repository = repository;
    this.securityDomain = securityDomain;
    this.authorizationService = authorizationService;
    this.parentEntities = null;
  }

  public JoinEntityHelper(AuthorizationService authorizationService,
                          String securityDomain,
                          ReadRepository<JE, JID> repository,
                          Collection<E> parentEntities) {
    this.repository = repository;
    this.securityDomain = securityDomain;
    this.authorizationService = authorizationService;
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
      Collection<JE> joined = repository.findById(permitted);
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
