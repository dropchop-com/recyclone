package com.dropchop.recyclone.base.dto.model;

import com.dropchop.recyclone.base.api.model.attr.AttributeDate;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 02. 22.
 */
class AttributeDateTest {

  @SuppressWarnings("unused")
  @Test
  void parseFromIsoString() {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .appendPattern("yyyy-MM-dd HH:mm:ss")
      .appendFraction(ChronoField.MILLI_OF_SECOND, 2, 3, true) // min 2 max 3
      .toFormatter();

    AttributeDate aDate = AttributeDate.parseFromIsoString("foo", "2022-01-22");
    assertNotNull(aDate);

    aDate = AttributeDate.parseFromIsoString("foo", "2022-01-22T23:55");
    assertNotNull(aDate);

    aDate = AttributeDate.parseFromIsoString("foo", "2022-01-22T23:55:59");
    assertNotNull(aDate);

    aDate = AttributeDate.parseFromIsoString("foo", "2022-01-22T23:55:59.8");
    assertNotNull(aDate);

    aDate = AttributeDate.parseFromIsoString("foo", "2022-01-22T23:55:59.897");
    assertNotNull(aDate);

    aDate = AttributeDate.parseFromIsoString("foo", "2022-01-22T23:55:59.897+02:00");
    assertNotNull(aDate);
  }
}