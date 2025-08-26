package com.dropchop.recyclone.base.jpa.repo.localization;

import com.dropchop.recyclone.base.jpa.model.localization.JpaDictionaryTerm;
import com.dropchop.recyclone.base.jpa.repo.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

@Getter
@ApplicationScoped
public class DictionaryTermRepository extends BlazeRepository<JpaDictionaryTerm, String> {
    Class<JpaDictionaryTerm> rootClass = JpaDictionaryTerm.class;
}
