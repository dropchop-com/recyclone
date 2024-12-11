package com.dropchop.recyclone.model.api.attr;

import com.dropchop.recyclone.model.api.utils.Iso8601;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class AttributeDate extends AttributeBase<ZonedDateTime> {

  /**
   * Parses date attribute from ISO 8601 string supporting shorter formats.
   * @param value string representing a date in iso format
   * @return AttributeDate or null if string is not date like.
   * @throws RuntimeException if string is date-like and can not be parsed.
   */
  public static AttributeDate parseFromIsoString(String name, String value) {
    ZonedDateTime date = Iso8601.fromIso(value);
    if (date == null) {
      return null;
    }
    return new AttributeDate(name, date);
  }

  @NonNull
  private ZonedDateTime value;

  public AttributeDate(@NonNull String name, @NonNull ZonedDateTime value) {
    super(name);
    this.value = value;
  }
}
