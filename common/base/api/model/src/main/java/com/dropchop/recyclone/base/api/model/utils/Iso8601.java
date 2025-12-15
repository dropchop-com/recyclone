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

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
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


  private static Duration parseDurationOrDefault(String isoDuration, Duration defaultDuration) {
    if (isoDuration == null || isoDuration.isBlank()) {
      return defaultDuration;
    }
    try {
      Duration d = Duration.parse(isoDuration.trim());
      // Disallow negative durations
      if (d.isNegative()) {
        return defaultDuration;
      }
      return d;
    } catch (DateTimeParseException ex) {
      return defaultDuration;
    }
  }

  /**
   * Computes endDate from startDate and ISO duration.
   *
   * @param startDate        The start of the window
   * @param maxEndDate       The maximum end date of the window (ZonedDateTime.now() is the default value if null)
   * @param isoDuration      ISO-8601 duration (e.g., "P10D", "PT5H"). It may be null/invalid.
   * @param defaultDuration  Duration used if isoDuration is invalid or unusable
   */
  static ZonedDateTime computeDurationEndDate(ZonedDateTime startDate, ZonedDateTime maxEndDate, String isoDuration,
                                              Duration defaultDuration) {
    java.util.Objects.requireNonNull(startDate, "startDate must not be null");
    java.util.Objects.requireNonNull(defaultDuration, "defaultDuration must not be null");

    Duration effectiveDuration = parseDurationOrDefault(isoDuration, defaultDuration);
    ZonedDateTime now = ZonedDateTime.now(startDate.getZone());
    ZonedDateTime endDate;

    try {
      endDate = startDate.plus(effectiveDuration);
    } catch (DateTimeException ex) {
      endDate = startDate.plus(defaultDuration);
    }

    // Clamp endDate so it cannot exceed "now"
    if (maxEndDate == null) {
      maxEndDate = now;
    } else if (maxEndDate.isAfter(now)) {
      maxEndDate = now;
    }

    if (endDate.isAfter(maxEndDate)) {
      endDate = maxEndDate;
    }

    return endDate;
  }

  /**
   * Computes endDate from startDate and ISO duration.
   *
   * @param endDate          The end of the window
   * @param maxStartDate     The maximum start date of the window (ZonedDateTime.now() is the default value if null)
   * @param isoDuration      ISO-8601 duration (e.g., "P10D", "PT5H"). It may be null/invalid.
   * @param defaultDuration  Duration used if isoDuration is invalid or unusable
   */
  static ZonedDateTime computeDurationStartDate(ZonedDateTime endDate, ZonedDateTime maxStartDate, String isoDuration,
                                                Duration defaultDuration) {
    java.util.Objects.requireNonNull(endDate, "endDate must not be null");
    java.util.Objects.requireNonNull(defaultDuration, "defaultDuration must not be null");

    Duration effectiveDuration = parseDurationOrDefault(isoDuration, defaultDuration);
    ZonedDateTime startDate;

    try {
      startDate = endDate.minus(effectiveDuration);
    } catch (DateTimeException ex) {
      startDate = endDate.plus(defaultDuration);
    }

    // Clamp maxStartDate so it cannot exceed "now"
    if (maxStartDate == null) {
      maxStartDate = ZonedDateTime.now(endDate.getZone());
    }
    if (startDate.isBefore(maxStartDate)) {
      startDate = maxStartDate;
    }

    return startDate;
  }

  /**
   * Aligns the given ZonedDateTime to the start of its day (00:00:00.000) in the same zone.
   *
   * @param dateTime the date-time to align (must not be null)
   * @return a new ZonedDateTime at 00:00:00.000 of the same day and zone
   */
  static ZonedDateTime alignToStartOfDay(ZonedDateTime dateTime) {
    java.util.Objects.requireNonNull(dateTime, "dateTime must not be null");
    return dateTime.truncatedTo(ChronoUnit.DAYS);
  }
}
