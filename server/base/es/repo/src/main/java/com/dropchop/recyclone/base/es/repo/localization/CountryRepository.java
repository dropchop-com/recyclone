package com.dropchop.recyclone.base.es.repo.localization;

import com.dropchop.recyclone.base.es.model.localization.EsCountry;
import com.dropchop.recyclone.base.es.repo.ElasticRepository;
import com.dropchop.recyclone.base.es.repo.mapper.ElasticQueryMapper;
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
  public ElasticQueryMapper getElasticQueryMapper() {
    return null;
  }

  @Override
  public ObjectMapper getObjectMapper() {
    return null;
  }

  @Override
  public RestClient getElasticsearchClient() {
    return null;
  }
}
