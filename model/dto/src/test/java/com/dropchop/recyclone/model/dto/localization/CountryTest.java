package com.dropchop.recyclone.model.dto.localization;

import org.junit.jupiter.api.Test;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
class CountryTest {
  @Test
  void construct() {
    Country country = Country
      .builder()
      .code("SI")
      .build();
  }
}