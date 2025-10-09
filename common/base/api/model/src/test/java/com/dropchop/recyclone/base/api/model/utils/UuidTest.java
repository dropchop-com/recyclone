package com.dropchop.recyclone.base.api.model.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Slf4j
class UuidTest {

  @Test
  void minimal() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat(Iso8601.DATETIME);
    String start = "2020-01-01T00:00:00";
    Date sdate = sdf.parse(start);
    Calendar c = Calendar.getInstance();
    c.setTime(sdate);
    for (int i = 0; i < 12; i++) {
      start = sdf.format(c.getTime());
      UUID suuid = Uuid.msAlignedMin(c.getTime().getTime());
      log.info("UUID [{}] = [{}]", start, suuid);
      c.add(Calendar.MONTH, 1);
    }
  }

  @Test
  void maximal() {
  }


  @Test
  void getNameBased() {
    UUID expected = UUID.fromString("4c4de73e-fbdc-321a-a15b-3c084204a915");
    UUID uuid = Uuid.getNameBasedV3("Language.sl");
    assertEquals(expected, uuid);
  }

  @Test
  void timebased() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat(Iso8601.DATETIME);
    String start = "2020-01-01T00:00:00";
    Date date = sdf.parse(start);
    Instant instant = date.toInstant();
    UUID uuid = Uuid.fromTimeAndName(instant, "test");
    Date test = new Date(Uuid.toUnixTimestamp(uuid));
    assertEquals(date, test);
  }

  @Test
  public void toUnixTimestamp() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat(Iso8601.DATETIME);
    UUID uuid = UUID.fromString("166aa70f-0847-11ea-bfff-c7873b8961e5");
    Date test = new Date(Uuid.toUnixTimestamp(uuid));
    Date date = sdf.parse("2019-11-16T09:00:00");
    assertEquals(date, test);
  }

  @Test
  public void toInstant() {
    UUID uuid = UUID.fromString("420312d3-a3d3-11f0-b1e4-1547566be87f");
    Instant instant = Uuid.toInstant(uuid);
    String pathParam = DateTimeFormatter.ofPattern("yyyy/MM/dd/")
        .withZone(ZoneId.of("Europe/Ljubljana"))
        .format(instant);
    assertEquals("2025/10/08/", pathParam);
    uuid = UUID.fromString("2cef5b11-a49d-11f0-8cb9-5b69404c04b7");
    instant = Uuid.toInstant(uuid);
    pathParam = DateTimeFormatter.ofPattern("yyyy/MM/dd/")
        .withZone(ZoneId.of("Europe/Ljubljana"))
        .format(instant);
    assertEquals("2025/10/09/", pathParam);
  }
}