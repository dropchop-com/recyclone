package com.dropchop.recyclone.quarkus.it.repo.es;

import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.repo.es.ElasticRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
public class ElasticDummyRepository extends ElasticRepository<Dummy, String> {

  Class<Dummy> rootClass = Dummy.class;

  @Override
  public Class<Dummy> getRootClass() {
    return rootClass;
  }

  @Override
  protected Map<String, Object> convertEntityToMap(Dummy entity) {
    return Map.of(
      "code", entity.getCode(),
      "title", entity.getTitle(),
      "description", entity.getDescription(),
      "lang", entity.getLang(),
      "created", entity.getCreated().toString(),
      "modified", entity.getModified().toString(),
      "deactivated", entity.getDeactivated() != null ? entity.getDeactivated().toString() : null
    );
  }

  @Override
  protected Dummy convertMapToEntity(Map<String, Object> source) {
    Dummy dummy = new Dummy();
    dummy.setCode((String) source.get("code"));
    dummy.setTitle((String) source.get("title"));
    dummy.setDescription((String) source.get("description"));
    dummy.setLang((String) source.get("lang"));
    return dummy;
  }

  @Override
  protected String getEntityId(Dummy entity) {
    return entity.getCode();
  }
}
