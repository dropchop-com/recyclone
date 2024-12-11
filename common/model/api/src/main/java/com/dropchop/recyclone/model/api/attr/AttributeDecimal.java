package com.dropchop.recyclone.model.api.attr;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
@SuppressWarnings("unused")
public class AttributeDecimal extends AttributeBase<BigDecimal> {
  @NonNull
  private BigDecimal value;

  public AttributeDecimal(@NonNull String name, @NonNull BigDecimal value) {
    super(name);
    this.value = value;
  }

  public AttributeDecimal(@NonNull String name, long value) {
    super(name);
    this.value = new BigDecimal(value);
  }

  public AttributeDecimal(@NonNull String name, int value) {
    super(name);
    this.value = new BigDecimal(value);
  }

  public AttributeDecimal(@NonNull String name, double value) {
    super(name);
    this.value = new BigDecimal(value);
  }

  public AttributeDecimal(@NonNull String name, float value) {
    super(name);
    this.value = new BigDecimal(value);
  }
}
