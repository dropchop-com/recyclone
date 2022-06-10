package com.dropchop.recyclone.service.jpa.blaze;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.rest.Constants.ContentDetail;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeExecContext;
import com.dropchop.recyclone.service.api.CrudService;
import com.dropchop.recyclone.service.api.EntityByIdService;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;
import com.dropchop.recyclone.service.api.invoke.FilteringDtoContext;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.EntityDelegateFactory;
import com.dropchop.recyclone.service.api.mapping.SetDeactivated;
import com.dropchop.recyclone.service.api.mapping.SetModification;
import com.dropchop.recyclone.service.jpa.blaze.localization.LanguageService;
import com.dropchop.recyclone.service.jpa.blaze.mapping.SetLanguage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 03. 22.
 */
@Slf4j
public abstract class CrudServiceImpl<D extends Dto, P extends Params, E extends Entity, ID>
  implements CrudService<D, P>, EntityByIdService<D, E, ID> {

  @Inject
  ServiceSelector serviceSelector;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContext<P, D> ctx;

  public abstract ServiceConfiguration<D, P, E, ID> getConfiguration();

  @Override
  public Class<E> getRootClass() {
    return getConfiguration().getRepository().getRootClass();
  }

  @Override
  public Optional<E> findById(D dto) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    if (dto instanceof DtoCode) {
      //noinspection unchecked
      return conf.getRepository().findById((ID)((DtoCode) dto).getCode());
    } else if (dto instanceof DtoId) {
      //noinspection unchecked
      return conf.getRepository().findById((ID)((DtoId) dto).getUuid());
    } else {
      throw new RuntimeException("findById(" + dto + ") is not supported.");
    }
  }

  @Override
  public List<E> findById(Collection<ID> ids) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    return conf.getRepository().findById(ids);
  }

  @Override
  public Optional<E> findById(ID id) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    return conf.getRepository().findById(id);
  }

  @Override
  public List<E> findAll() {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    return conf.getRepository().find();
  }

  protected void checkDtoPermissions(List<D> dtos) {
    Subject subject = ctx.getSubject();
    for (D dto : dtos) {
      if (!subject.isPermitted(ctx.getSecurityDomainAction(dto.identifier()))) {
        throw new ServiceException(ErrorCode.authorization_error, "Not permitted!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
    }
  }

  protected List<E> find(RepositoryExecContext<E, P> repositoryExecContext) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    if (repositoryExecContext == null) {
      repositoryExecContext = new BlazeExecContext<E, P>().of(ctx)
        .criteriaDecorators(conf.getCriteriaDecorators());
    }
    return conf.getRepository().find(repositoryExecContext);
  }

  protected List<E> find() {
    return find(null);
  }

  @Override
  public Result<D> search() {
    Subject subject = ctx.getSubject();
    if (subject.isPermitted(ctx.getSecurityDomainAction())) {
      log.trace("search [{}] is permitted to view [{}]!", subject.getPrincipal(), ctx.getParams());
    }
    MappingContext<P> mapContext = new FilteringDtoContext<P>().of(ctx);
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    List<E> entities = find(new BlazeExecContext<E, P>()
      .of(ctx)
      .listener(mapContext) // get total count and save it
      .criteriaDecorators(conf.getCriteriaDecorators())
    );
    entities = entities.stream().filter(
      e -> subject.isPermitted(ctx.getSecurityDomainAction(e.identifier()))
    ).collect(Collectors.toList());

    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }

  protected MappingContext<P> constructToEntityMappingContext(ServiceConfiguration<D, P, E, ID> conf) {
    Class<?> rootClass = conf.getRepository().getRootClass();
    return new FilteringDtoContext<P>()
      .of(ctx)
      .createWith(
        new EntityDelegateFactory<D, E, ID, P>(this)
          .forActionOnly(Constants.Actions.UPDATE)
          .forActionOnly(Constants.Actions.DELETE)
      )
      .afterMapping(
        new SetModification<>(rootClass)
      )
      .afterMapping(
        new SetLanguage<>(serviceSelector.select(LanguageService.class), rootClass)
      )
      .afterMapping(
        new SetDeactivated<>(rootClass)
      );
  }

  protected boolean shouldRefreshAfterSave() {
    P params = ctx.getParams();
    String cDetail = params.getContentDetailLevel();
    if (cDetail == null) {
      cDetail = ContentDetail.NESTED_OBJS_IDCODE;
    }
    Integer cLevel = params.getContentTreeLevel();
    if (cLevel == null) {
      cLevel = 1;
    }
    if (cLevel >= 2) {
      return true;
    }
    return cLevel == 1 &&
      (
        ContentDetail.ALL_OBJS_IDCODE_TITLE.equals(cDetail) ||
        ContentDetail.NESTED_OBJS_IDCODE.equals(cDetail) ||
        ContentDetail.NESTED_OBJS_IDCODE_TITLE.equals(cDetail)
      );
  }

  protected void save(Collection<E> entities) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    CrudRepository<E, ID> repository = conf.getRepository();
    repository.save(entities);
    if (shouldRefreshAfterSave()) {
      repository.refresh(entities);
    }
  }

  protected Result<D> createOrUpdate(List<D> dtos) {
    checkDtoPermissions(dtos);
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    MappingContext<P> mapContext = constructToEntityMappingContext(conf);
    List<E> entities = conf.getToEntityMapper().toEntities(dtos, mapContext);
    save(entities);
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }

  @Override
  @Transactional
  public Result<D> create(List<D> dtos) {
    return createOrUpdate(dtos);
  }

  @Override
  @Transactional
  public Result<D> update(List<D> dtos) {
    return createOrUpdate(dtos);
  }

  @Override
  @Transactional
  public Result<D> delete(List<D> dtos) {
    checkDtoPermissions(dtos);
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration();
    MappingContext<P> mapContext = constructToEntityMappingContext(conf);
    List<E> entities = conf.getToEntityMapper().toEntities(dtos, mapContext);
    conf.getRepository().delete(entities);
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }
}
