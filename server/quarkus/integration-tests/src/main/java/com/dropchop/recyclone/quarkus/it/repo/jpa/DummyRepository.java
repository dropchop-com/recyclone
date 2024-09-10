package com.dropchop.recyclone.quarkus.it.repo.jpa;

import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaDummy;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
public class DummyRepository extends BlazeRepository<JpaDummy, String>
    implements com.dropchop.recyclone.quarkus.it.repo.DummyRepository {

  Class<JpaDummy> rootClass = JpaDummy.class;
}
