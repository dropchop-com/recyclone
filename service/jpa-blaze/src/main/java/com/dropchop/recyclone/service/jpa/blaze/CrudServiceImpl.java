package com.dropchop.recyclone.service.jpa.blaze;

import com.dropchop.recyclone.model.api.Dto;
import com.dropchop.recyclone.model.api.Entity;
import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.dto.DtoCode;
import com.dropchop.recyclone.model.dto.DtoId;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeExecContext;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.CrudService;
import com.dropchop.recyclone.service.api.EntityByIdService;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.localization.LanguageService;
import com.dropchop.recyclone.service.api.mapping.FilteringDtoContext;
import com.dropchop.recyclone.service.api.mapping.MappingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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

  public abstract ServiceConfiguration<D, P, E, ID> getConfiguration(CommonExecContext<P, D> ctx);


  @Override
  public Optional<E> findById(D dto) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration(ctx);
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
  public List<E> findById(List<ID> ids) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration(ctx);
    return conf.getRepository().findById(ids);
  }

  @Override
  public Optional<E> findById(ID id) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration(ctx);
    return conf.getRepository().findById(id);
  }

  @Override
  public List<E> findAll() {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration(ctx);
    return conf.getRepository().find();
  }

  @Override
  public Result<D> search() {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration(ctx);

    Subject subject = ctx.getSubject();
    if (subject.isPermitted(ctx.getSecurityDomainAction())) {
      log.trace("search [{}] is permitted to view [{}]!", subject.getPrincipal(), ctx.getParams());
    }

    MappingContext<P> mapContext = new FilteringDtoContext<P>()
      .of(ctx);

    List<E> entities = conf.getRepository().find(
      new BlazeExecContext<E, P>()
        .of(ctx)
        .listener(mapContext)
        .criteriaDecorators(conf.getCriteriaDecorators())
    );

    entities.removeIf(e -> !subject.isPermitted(ctx.getSecurityDomainAction(e.identifier())));
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }

  private MappingContext<P> checkPermissionsAndConstructMapping(List<D> dtos) {
    Subject subject = ctx.getSubject();
    for (D dto : dtos) {
      if (!subject.isPermitted(ctx.getSecurityDomainAction(dto.identifier()))) {
        throw new ServiceException(ErrorCode.authorization_error, "Not permitted!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
    }

    return new FilteringDtoContext<P>()
      .of(ctx)
      .listener(new CrudServiceToEntityListener<>(
        serviceSelector.select(LanguageService.class).findAll()
          .stream()
          .collect(Collectors.toMap(ELanguage::getCode, Function.identity()))
      ));
  }

  @Override
  @Transactional
  public Result<D> create(List<D> dtos) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration(ctx);
    MappingContext<P> mapContext = checkPermissionsAndConstructMapping(dtos);

    List<E> entities = conf.getToEntityMapper().toEntities(dtos, mapContext);
    conf.getRepository().save(entities);
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }

  @Override
  @Transactional
  public Result<D> update(List<D> dtos) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration(ctx);

    MappingContext<P> mapContext = checkPermissionsAndConstructMapping(dtos);

    List<E> entities = conf.getToEntityMapper()
      .updateEntities(dtos, this::findById, mapContext);
    conf.getRepository().save(entities);
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }

  @Override
  @Transactional
  public Result<D> delete(List<D> dtos) {
    ServiceConfiguration<D, P, E, ID> conf = getConfiguration(ctx);

    MappingContext<P> mapContext = checkPermissionsAndConstructMapping(dtos);

    List<E> entities = conf.getToEntityMapper().updateEntities(dtos,
      dto -> {
        Optional<E> oEntity = findById(dto);
        if (oEntity.isPresent()) {
          conf.getRepository().delete(oEntity.get());
        } else {
          log.warn("Missing entity for dto [{}]", dto);
        }
        return oEntity;
      },
      mapContext);
    return conf.getToDtoMapper().toDtosResult(entities, mapContext);
  }
}
