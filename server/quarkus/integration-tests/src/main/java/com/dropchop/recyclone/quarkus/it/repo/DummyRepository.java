package com.dropchop.recyclone.quarkus.it.repo;

import com.dropchop.recyclone.quarkus.it.model.dto.Dummy;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.repo.api.CrudServiceRepository;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 06. 24.
 */
public interface DummyRepository extends CrudServiceRepository<Dummy, JpaDummy, String> {
}
