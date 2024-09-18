package com.dropchop.recyclone.repo.es.localization;

import com.dropchop.recyclone.model.entity.es.localization.EsCountry;
import com.dropchop.recyclone.repo.es.ElasticRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@Getter
@ApplicationScoped
public class CountryRepository extends ElasticRepository<EsCountry, String> {

  Class<EsCountry> rootClass = EsCountry.class;
}
