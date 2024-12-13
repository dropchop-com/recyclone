package com.dropchop.recyclone.base.api.model.utils;

/*
 * Apache License, Version 2.0
 *
 * Copyright (c) 2011, Dropchop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16.9.2015.
 */
@SuppressWarnings("unused")
public interface Iso8601 {
  String DATE = "yyyy-MM-dd";
  String TIME_SHORT = "HH:mm";
  String TIME = "HH:mm:ss";
  String TZ = "XXX";
  String MS = "[SSS]";
  String DATETIME = DATE + "'T'" + TIME;
  String DATETIME_SHORT = DATE + "'T'" + TIME_SHORT;
  String DATETIME_TZ = DATETIME + TZ;
  String DATETIME_MS = DATETIME + "." + MS;
  String DATETIME_MS_TZ = DATETIME + "." + MS + TZ;


  ThreadLocal<DateTimeFormatter> DATE_FORMATTER =
    ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern(DATE));

  ThreadLocal<DateTimeFormatter> DATE_TIME_SHORT_FORMATTER =
    ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern(DATETIME_SHORT));

  ThreadLocal<DateTimeFormatter> DATE_TIME_FORMATTER =
    ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern(DATETIME));

  ThreadLocal<DateTimeFormatter> DATE_TIME_MS_FORMATTER =
    ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern(DATETIME_MS));

  ThreadLocal<DateTimeFormatter> DATE_TIME_TZ_FORMATTER =
    ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern(DATETIME_TZ));

  ThreadLocal<DateTimeFormatter> DATE_TIME_MS_TZ_FORMATTER =
    ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern(DATETIME_MS_TZ));

  Pattern dateTimePattern = Pattern.compile("[0-9]{4}-[01][0-9]-[0-3][0-9](T[0-2][0-9]:[0-5][0-9](:[0-5][0-9](\\.[0-9]{1,3})*(.)*)*)*",
    Pattern.MULTILINE);

  static Matcher matchIso(String value) {
    return dateTimePattern.matcher(value);
  }

  static boolean isIso(String value) {
    return matchIso(value).matches();
  }

  /**
   * Parses date attribute from ISO 8601 string supporting shorter formats.
   * @param matcher Regex Matcher returned from @see matchIso
   * @return ZonedDateTime or null if string is not date like.
   * @throws RuntimeException if string is date-like and can not be parsed.
   */
  static ZonedDateTime fromMatchedIso(Matcher matcher) {
    if (!matcher.matches()) {
      return null;
    }

    String completeStr = matcher.group(0);
    String timeStr = matcher.group(1);
    String secsStr = matcher.group(2);
    String msecStr = matcher.group(3);
    String zoneStr = matcher.group(4);
    try {
      if (zoneStr != null) {
        return ZonedDateTime.parse(completeStr);
      } else if (msecStr != null) {
        return LocalDateTime.parse(completeStr).atZone(ZoneId.systemDefault());
      } else if (secsStr != null) {
        return LocalDateTime.parse(completeStr).atZone(ZoneId.systemDefault());
      } else if (timeStr != null) {
        return LocalDateTime.parse(completeStr).atZone(ZoneId.systemDefault());
      } else {
        return LocalDate.parse(completeStr).atTime(0,0).atZone(ZoneId.systemDefault());
      }
    } catch (DateTimeParseException e) {
      throw new RuntimeException("Invalid date string value [" + matcher.group() + "]!", e);
    }
  }

  /**
   * Parses date attribute from ISO 8601 string supporting shorter formats.
   * @param value string representing a date in iso format
   * @return ZonedDateTime or null if string is not date like.
   * @throws RuntimeException if string is date-like and can not be parsed.
   */
  static ZonedDateTime fromIso(String value) {
    return fromMatchedIso(matchIso(value));
  }
}
