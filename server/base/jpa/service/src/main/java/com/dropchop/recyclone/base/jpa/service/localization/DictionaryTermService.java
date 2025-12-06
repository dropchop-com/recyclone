package com.dropchop.recyclone.base.jpa.service.localization;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.dto.model.localization.DictionaryTerm;
import com.dropchop.recyclone.base.jpa.model.localization.JpaDictionaryTerm;
import com.dropchop.recyclone.base.jpa.repo.localization.DictionaryTermMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.localization.DictionaryTermRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Getter
@RequestScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class DictionaryTermService extends CrudServiceImpl<DictionaryTerm, JpaDictionaryTerm, String>
  implements com.dropchop.recyclone.base.api.service.localization.DictionaryTermService {

  @Inject
  DictionaryTermRepository repository;

  @Inject
  DictionaryTermMapperProvider mapperProvider;

  @Inject
  CommonExecContext<DictionaryTerm, ?> executionContext;
}
