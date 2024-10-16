package com.dropchop.recyclone.repo.es.localization;

import com.dropchop.recyclone.model.entity.es.localization.EsCountry;
import com.dropchop.recyclone.repo.es.ElasticRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@Getter
@ApplicationScoped
public class CountryRepository extends ElasticRepository<EsCountry, String> {

  Class<EsCountry> rootClass = EsCountry.class;

  @Override
  protected Map<String, Object> convertEntityToMap(EsCountry entity) {
    return Map.of();
  }

  @Override
  protected EsCountry convertMapToEntity(Map<String, Object> source) {
    return null;
  }

  @Override
  protected String getEntityId(EsCountry entity) {
    return "";
  }
}
