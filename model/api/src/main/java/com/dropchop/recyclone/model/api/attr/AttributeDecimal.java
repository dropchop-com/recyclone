package com.dropchop.recyclone.model.api.attr;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AttributeDecimal extends AttributeBase<BigDecimal> {
  @NonNull
  private BigDecimal value;

  public AttributeDecimal(@NonNull String name, @NonNull BigDecimal value) {
    super(name);
    this.value = value;
  }


  public AttributeDecimal(@NonNull String name, @NonNull long value) {
    super(name);
    this.value = new BigDecimal(value);
  }


  public AttributeDecimal(@NonNull String name, @NonNull int value) {
    super(name);
    this.value = new BigDecimal(value);
  }


  public AttributeDecimal(@NonNull String name, @NonNull double value) {
    super(name);
    this.value = new BigDecimal(value);
  }


  public AttributeDecimal(@NonNull String name, @NonNull float value) {
    super(name);
    this.value = new BigDecimal(value);
  }
}
