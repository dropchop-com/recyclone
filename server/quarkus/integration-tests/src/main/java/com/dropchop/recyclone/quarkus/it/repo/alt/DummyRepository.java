package com.dropchop.recyclone.quarkus.it.repo.alt;

import com.dropchop.recyclone.quarkus.it.mapper.jpa.DummyToDtoMapper;
import com.dropchop.recyclone.quarkus.it.mapper.jpa.DummyToJpaMapper;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
@RecycloneType("alter")
public class DummyRepository extends BlazeRepository<JpaDummy, String>
    implements com.dropchop.recyclone.quarkus.it.repo.DummyRepository {

  Class<JpaDummy> rootClass = JpaDummy.class;

  @Inject
  DummyToDtoMapper toDtoMapper;

  @Inject
  DummyToJpaMapper toEntityMapper;
}
