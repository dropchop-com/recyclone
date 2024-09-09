package com.dropchop.recyclone.repo.jpa.blaze.tagging;

import com.dropchop.recyclone.mapper.jpa.tagging.TagToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.tagging.TagToJpaMapper;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import com.dropchop.recyclone.repo.api.CrudServiceRepository;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import com.dropchop.recyclone.repo.jpa.blaze.LikeTypeCriteriaDecorator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 22.
 */
@Getter
@ApplicationScoped
public class TagRepository extends BlazeRepository<JpaTag, UUID>
    implements CrudServiceRepository<Tag, JpaTag, UUID> {

  Class<JpaTag> rootClass = JpaTag.class;

  @Inject
  TagToDtoMapper toDtoMapper;

  @Inject
  TagToJpaMapper toEntityMapper;

  @Override
  protected Collection<CriteriaDecorator> getCommonCriteriaDecorators() {
    Collection<CriteriaDecorator> commonDecorators = super.getCommonCriteriaDecorators();
    ArrayList<CriteriaDecorator> tagDecorators = new ArrayList<>(commonDecorators);
    tagDecorators.add(new LikeTypeCriteriaDecorator());
    return tagDecorators;
  }
}
