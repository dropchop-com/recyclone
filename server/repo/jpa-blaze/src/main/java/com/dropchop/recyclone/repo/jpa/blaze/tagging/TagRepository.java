package com.dropchop.recyclone.repo.jpa.blaze.tagging;

import com.dropchop.recyclone.mapper.jpa.tagging.TagToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.tagging.TagToJpaMapper;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.security.JpaPermission;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaTag;
import com.dropchop.recyclone.repo.api.CrudServiceRepository;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 22.
 */
@Getter
@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
public class TagRepository extends BlazeRepository<JpaTag, UUID>
    implements CrudServiceRepository<Tag, JpaTag, UUID> {

  Class<JpaTag> rootClass = JpaTag.class;

  @Inject
  TagToDtoMapper toDtoMapper;

  @Inject
  TagToJpaMapper toEntityMapper;
}
