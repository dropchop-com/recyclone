package com.dropchop.recyclone.quarkus.it.repo;

import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.repo.api.CrudRepository;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 06. 24.
 */
public interface DummyRepository extends CrudRepository<JpaDummy, String> {
}
