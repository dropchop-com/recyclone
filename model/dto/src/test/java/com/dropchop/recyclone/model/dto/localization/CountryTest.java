package com.dropchop.recyclone.model.dto.localization;

import org.junit.jupiter.api.Test;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
class CountryTest {
  @Test
  void construct() {
    Country country = new Country("SI");
    /*String id = country.getId();
    assertEquals(Uuid.getNameBasedV3(Country.class, "SI").toString(), id);
    assertEquals(id, country.getUuid().toString());*/
  }
}