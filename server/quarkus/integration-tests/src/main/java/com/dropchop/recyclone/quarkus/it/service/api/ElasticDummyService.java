package com.dropchop.recyclone.quarkus.it.service.api;

public interface ElasticDummyService extends DummyService {

  Integer deleteByQuery();
  Integer deleteById();
}
