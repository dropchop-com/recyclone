package com.dropchop.recyclone.model.api.attr;

import lombok.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;

import static com.dropchop.recyclone.model.api.utils.Iso8601.dateTimePattern;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AttributeDate extends AttributeBase<ZonedDateTime> {

  /**
   * Parses date attribute from ISO 8601 string supporting shorter formats.
   * @param value string representing a date in iso format
   * @return AttributeDate or null if string is not date like.
   * @throws IOException if string is date-like and can not be parsed.
   */
  public static AttributeDate parseFromIsoString(String name, String value) throws IOException {
    Matcher matcher = dateTimePattern.matcher(value);
    if (matcher.matches()) {
      String completeStr = matcher.group(0);
      String timeStr = matcher.group(1);
      String secsStr = matcher.group(2);
      String msecStr = matcher.group(3);
      String zoneStr = matcher.group(4);
      try {
        if (zoneStr != null) {
          return new AttributeDate(name, ZonedDateTime.parse(completeStr));
        } else if (msecStr != null) {
          return new AttributeDate(name, LocalDateTime.parse(completeStr).atZone(ZoneId.systemDefault()));
        } else if (secsStr != null) {
          return new AttributeDate(name, LocalDateTime.parse(completeStr).atZone(ZoneId.systemDefault()));
        } else if (timeStr != null) {
          return new AttributeDate(name, LocalDateTime.parse(completeStr).atZone(ZoneId.systemDefault()));
        } else {
          return new AttributeDate(name, LocalDate.parse(completeStr).atTime(0,0).atZone(ZoneId.systemDefault()));
        }
      } catch (Exception e) {
        throw new IOException("Invalid date string value [" + value + "]!", e);
      }
    }
    return null;
  }

  @NonNull
  private ZonedDateTime value;

  public AttributeDate(@NonNull String name, @NonNull ZonedDateTime value) {
    super(name);
    this.value = value;
  }
}
