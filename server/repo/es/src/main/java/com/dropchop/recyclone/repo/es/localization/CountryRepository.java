package com.dropchop.recyclone.repo.es.localization;

import com.dropchop.recyclone.model.entity.es.localization.EsCountry;
import com.dropchop.recyclone.repo.es.ElasticRepository;
import com.dropchop.recyclone.repo.es.mapper.ElasticQueryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.elasticsearch.client.RestClient;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 09. 24.
 */
@Getter
@ApplicationScoped
public class CountryRepository extends ElasticRepository<EsCountry, String> {

  Class<EsCountry> rootClass = EsCountry.class;

  @Override
  protected ElasticQueryMapper getElasticQueryMapper() {
    return null;
  }

  @Override
  protected ObjectMapper getObjectMapper() {
    return null;
  }

  @Override
  protected RestClient getElasticsearchClient() {
    return null;
  }
}
