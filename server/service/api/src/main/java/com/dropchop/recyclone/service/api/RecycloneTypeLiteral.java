package com.dropchop.recyclone.service.api;

import jakarta.enterprise.util.AnnotationLiteral;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 21. 04. 22.
 */
public class RecycloneTypeLiteral extends AnnotationLiteral<RecycloneType> implements RecycloneType {

  private static final long serialVersionUID = 1L;
  private final String value;

  public RecycloneTypeLiteral(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return this.value;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof RecycloneType) {
      RecycloneType otherType = (RecycloneType) other;
      return value.equals(otherType.value());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
