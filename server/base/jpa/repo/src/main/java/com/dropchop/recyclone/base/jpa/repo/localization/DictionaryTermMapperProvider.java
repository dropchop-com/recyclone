package com.dropchop.recyclone.base.jpa.repo.localization;

import com.dropchop.recyclone.base.dto.model.localization.DictionaryTerm;
import com.dropchop.recyclone.base.jpa.mapper.localization.DictionaryTermToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.localization.DictionaryTermToJpaMapper;
import com.dropchop.recyclone.base.jpa.model.localization.JpaDictionaryTerm;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

@Getter
@ApplicationScoped
public class DictionaryTermMapperProvider extends RecycloneMapperProvider<DictionaryTerm, JpaDictionaryTerm, String> {


    @Inject
    DictionaryTermRepository repository;

    @Inject
    DictionaryTermToDtoMapper toDtoMapper;

    @Inject
    DictionaryTermToJpaMapper toEntityMapper;

}
