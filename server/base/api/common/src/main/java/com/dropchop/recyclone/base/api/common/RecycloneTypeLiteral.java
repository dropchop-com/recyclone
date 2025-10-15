package com.dropchop.recyclone.base.api.common;

import jakarta.enterprise.util.AnnotationLiteral;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 15. 10. 2025.
 */
public class RecycloneTypeLiteral extends AnnotationLiteral<RecycloneType> implements RecycloneType {

  private final String value;

  public static RecycloneTypeLiteral of(String value) {
    return new RecycloneTypeLiteral(value);
  }

  private RecycloneTypeLiteral(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return value;
  }
}
