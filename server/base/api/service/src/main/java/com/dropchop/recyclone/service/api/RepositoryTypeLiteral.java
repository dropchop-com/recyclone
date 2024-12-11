package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.repo.api.RepositoryType;
import jakarta.enterprise.util.AnnotationLiteral;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 21. 04. 22.
 */
@SuppressWarnings({"ClassExplicitlyAnnotation", "unused"})
public class RepositoryTypeLiteral extends AnnotationLiteral<RepositoryType> implements RepositoryType {

  private final String value;

  public RepositoryTypeLiteral(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return this.value;
  }
}
