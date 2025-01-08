package com.dropchop.recyclone.base.jpa.service.tagging;

import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.api.service.RecycloneType;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.dropchop.recyclone.base.jpa.model.tagging.JpaTag;
import com.dropchop.recyclone.base.jpa.repo.tagging.TagMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.tagging.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;


/**
 * TODO: implement tagging of tags then generalize it and implement logic in CrudServiceImpl
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class TagService extends CrudServiceImpl<Tag, JpaTag, UUID>
  implements com.dropchop.recyclone.base.api.service.tagging.TagService {

  @Inject
  TagRepository repository;

  @Inject
  TagMapperProvider mapperProvider;

  @Inject
  CommonExecContext<Tag, ?> executionContext;
}
