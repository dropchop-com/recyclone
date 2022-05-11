package com.dropchop.recyclone.model.api.utils;

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

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16.9.2015.
 */
@SuppressWarnings("unused")
public interface Iso8601 {
  String DATE = "yyyy-MM-dd";
  String TIME_SHORT = "HH:mm";
  String TIME = "HH:mm:ss";
  String TZ = "V";
  String MS = "[SSS][SS][S]";
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
}
