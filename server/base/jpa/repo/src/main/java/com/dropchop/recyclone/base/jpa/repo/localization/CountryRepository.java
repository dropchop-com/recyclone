package com.dropchop.recyclone.base.jpa.repo.localization;

import com.dropchop.recyclone.model.entity.jpa.localization.JpaCountry;
import com.dropchop.recyclone.base.jpa.repo.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
public class CountryRepository extends BlazeRepository<JpaCountry, String> {

  Class<JpaCountry> rootClass = JpaCountry.class;
}
