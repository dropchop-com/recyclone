package com.dropchop.recyclone.service.api;

import javax.enterprise.util.AnnotationLiteral;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 21. 04. 22.
 */
public class ServiceTypeLiteral extends AnnotationLiteral<ServiceType> implements ServiceType {

  private final String value;


  public ServiceTypeLiteral(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return this.value;
  }
}
