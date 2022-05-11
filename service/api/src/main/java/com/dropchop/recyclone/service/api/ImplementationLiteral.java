package com.dropchop.recyclone.service.api;

import javax.enterprise.util.AnnotationLiteral;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 21. 04. 22.
 */
public class ImplementationLiteral extends AnnotationLiteral<Implementation> implements Implementation {

  private final String value;


  public ImplementationLiteral(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return this.value;
  }
}
